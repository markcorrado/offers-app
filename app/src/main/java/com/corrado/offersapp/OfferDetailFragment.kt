package com.corrado.offersapp

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Displays the details of an Offer
 * Is passed a offerId and uses that to query the DB and display the result
 * Allows the user to toggle a favorite
 */
class OfferDetailFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var termsTextView: TextView
    private lateinit var valueTextView: TextView
    private lateinit var favoriteToggleButton: ToggleButton
    private lateinit var toolbar: Toolbar
    private var db: OfferDataBase? = null
    private val uiHandler = Handler()
    private var offerId: Long? = 0
    private var offerData: OfferData? = null
    private lateinit var workerThread: WorkerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerId = arguments?.getLong("offerId")
        db = OfferDataBase.getInstance(this.context!!)

        workerThread = WorkerThread("workerThread")
        workerThread.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offer_detail, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        imageView = view.findViewById(R.id.image_view)
        nameTextView = view.findViewById(R.id.name_text_view)
        descriptionTextView = view.findViewById(R.id.description_text_view)
        termsTextView = view.findViewById(R.id.terms_text_view)
        valueTextView = view.findViewById(R.id.amount_text_view)
        favoriteToggleButton = view.findViewById(R.id.favorite_button) as ToggleButton
        favoriteToggleButton.setOnCheckedChangeListener{view, isChecked ->
            offerData?.let {
                //Updating OfferData's favorite and saving in db
                it.isFavorite = isChecked
                val task = Runnable { db?.offerDataDao()?.updateOfferData(offerData!!) }
                workerThread.postTask(task)
            }
        }
        return view
    }

    override fun onDestroy() {
        workerThread.quit()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        getOfferDataFromDatabase(offerId)
    }

    /**
     * Gets the OfferData from DB and updates UI
     */
    private fun getOfferDataFromDatabase(offerId: Long?) {
        val task = Runnable {
            offerData = db?.offerDataDao()?.getOfferDataById(offerId!!)
            uiHandler.post {
                updateUI()
            }
        }
        workerThread.postTask(task)
    }

    private fun updateUI() {
        offerData?.let {
            toolbar.title = it.name
            valueTextView.text = it.currentValue
            nameTextView.text = it.name
            descriptionTextView.text = it.description
            termsTextView.text = it.terms
            favoriteToggleButton.isChecked = it.isFavorite
//          Using Picasso to load the image. Is this good enough?
            Picasso.get().load(it.url).placeholder(R.drawable.ic_launcher_background).into(imageView)
        }
    }
}
