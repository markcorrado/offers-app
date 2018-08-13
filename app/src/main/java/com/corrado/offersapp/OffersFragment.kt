package com.corrado.offersapp

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import org.json.JSONArray
import java.io.InputStream
import java.util.*

private const val TAG = "OffersFragment"

/**
 * A simple [Fragment] subclass.
 * Displays a list of Offers
 *
 */
class OffersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var toolbar: Toolbar
    private var db: OfferDataBase? = null
    //Using to communicate to UI thread
    private val uiHandler = Handler()
    //Using to do background work.
    private lateinit var workerThread: WorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workerThread = WorkerThread("workerThread")
        workerThread.start()

        db = OfferDataBase.getInstance(this.context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Offers"
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 2)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        checkOfferDataInDatabase()
    }

    override fun onDestroy() {
        //Clean up DB and thread
        OfferDataBase.destroyInstance()
        workerThread.quit()
        super.onDestroy()
    }

    /**
     * Reads our OfferData from the JSON file and inserts it into the DB
     */
    private fun readJson() {
        try {
            val inputStream: InputStream? = this.context?.assets?.open("Offers.json")
            val inputString = inputStream?.bufferedReader().use{ it?.readText() }
            val gson = Gson()
            val jsonArray = JSONArray(inputString)
            val offers = ArrayList<OfferData>()

            for (i in 0..(jsonArray.length() - 1)) {
                val item = jsonArray.getJSONObject(i)
                val offerData = gson.fromJson(item.toString(), OfferData::class.java)
                Log.d(TAG,offerData.toString())
                offers.add(offerData)
                insertOfferDataInDatabase(offerData)
            }

            updateUI(view, offers)
            Log.d(TAG,inputString)
        } catch (e:Exception){
            Log.d(TAG, e.message)
        }
    }

    /**
     * Updates RecyclerView
     */
    private fun updateUI(view: View?, offers: ArrayList<OfferData>) {
        viewAdapter = OffersAdapter(offers) { offerItem : OfferData -> offerItemClicked(view!!, offerItem) }
        recyclerView.adapter = viewAdapter
    }

    /**
     * Inserts OfferData in DB using background thread
     */
    private fun insertOfferDataInDatabase(offerData: OfferData) {
        val task = Runnable { db?.offerDataDao()?.insert(offerData) }
        workerThread.postTask(task)
    }

    /**
     * Checking if OfferData is in DB, if it is, update the UI with the data
     * If we do not have the offerdata, it will be read from the json file
     * TODO: If we were concerned about stale data, we could add a call to an api here
     */
    private fun checkOfferDataInDatabase() {
        val task = Runnable {
            val offerDatas = db?.offerDataDao()?.getAll()
            uiHandler.post {
                if(offerDatas?.isEmpty()!!){
                    readJson()
                } else {
                    updateUI(view, offerDatas as ArrayList<OfferData>)
                }
            }
        }
        workerThread.postTask(task)
    }

    /**
     * Using Navigation and NavController to push to OfferDetail
     * See Navigation Architecture Components docs
     * Passing offerId to OfferDetail so it can know which OfferData to display
     */
    private fun offerItemClicked(view: View, offerItem : OfferData) {
        val bundle = Bundle()
        bundle.putLong("offerId", offerItem.id!!)
        Navigation.findNavController(view).navigate(R.id.action_offersFragment_to_offerDetailFragment, bundle)
    }
}
