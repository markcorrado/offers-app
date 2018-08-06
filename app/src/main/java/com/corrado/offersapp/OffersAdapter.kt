package com.corrado.offersapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class OffersAdapter(private val myDataset: ArrayList<OfferData>) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById(R.id.image_view) as ImageView
        val offerTextView = view.findViewById(R.id.offer_text_view) as TextView
        val descriptionTextView = view.findViewById(R.id.description_text_view) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.offer_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.offerTextView.text = myDataset[position].name
        holder.descriptionTextView.text = myDataset[position].description
    }

    override fun getItemCount() = myDataset.size
}