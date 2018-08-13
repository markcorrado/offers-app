package com.corrado.offersapp

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import org.json.JSONArray
import java.io.InputStream

private const val TAG = "OffersFragment"

/**
 * A simple [Fragment] subclass.
 * Displays a list of Offers
 *
 */
class OffersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: GridLayoutManager
    private lateinit var workerThread: WorkerThread
    private var db: OfferDataBase? = null
    private val uiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workerThread = WorkerThread("workerThread")
        workerThread.start()

        db = OfferDataBase.getInstance(this.context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)

        viewManager = GridLayoutManager(this.context, 2)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        checkOfferDataInDatabase()
    }

    override fun onDestroy() {
        OfferDataBase.destroyInstance()
        workerThread.quit()
        super.onDestroy()
    }

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

    private fun updateUI(view: View?, offers: ArrayList<OfferData>) {
        viewAdapter = OffersAdapter(offers) { offerItem : OfferData -> offerItemClicked(view!!, offerItem) }
        recyclerView.adapter = viewAdapter
    }

    private fun insertOfferDataInDatabase(offerData: OfferData) {
        val task = Runnable { db?.offerDataDao()?.insert(offerData) }
        workerThread.postTask(task)
    }

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

    private fun offerItemClicked(view: View, offerItem : OfferData) {
        val bundle = Bundle()
        bundle.putLong("offerId", offerItem.id!!)
        Navigation.findNavController(view).navigate(R.id.action_offersFragment_to_offerDetailFragment, bundle)
    }
}
