package com.corrado.offersapp

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 * Displays the details of an Offer
 */
class OfferDetailFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var termsTextView: TextView
    private lateinit var valueTextView: TextView
    private var db: OfferDataBase? = null
    private val uiHandler = Handler()
    private var offerId: Long? = 0
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
        imageView = view.findViewById(R.id.image_view)
        nameTextView = view.findViewById(R.id.name_text_view)
        descriptionTextView = view.findViewById(R.id.description_text_view)
        termsTextView = view.findViewById(R.id.terms_text_view)
        valueTextView = view.findViewById(R.id.amount_text_view)
        val favoriteButton = view.findViewById(R.id.favorite_button) as Button
        favoriteButton.setOnClickListener { v ->
            Toast.makeText(v.context, offerId.toString(), Toast.LENGTH_SHORT).show()
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

    private fun getOfferDataFromDatabase(offerId: Long?) {
        val task = Runnable {
            val offerData = db?.offerDataDao()?.getOfferDataById(offerId!!)
            uiHandler.post {
                updateUI(offerData!!)
            }
        }
        workerThread.postTask(task)
    }

    private fun updateUI(offerData: OfferData) {
        valueTextView.text = offerData.currentValue
        nameTextView.text = offerData.name
        descriptionTextView.text = offerData.description
        termsTextView.text = offerData.terms

//          Using Picasso to load the image. Is this good enough?
        Picasso.get().load(offerData.url).placeholder(R.drawable.ic_launcher_background).into(imageView)
    }
}
