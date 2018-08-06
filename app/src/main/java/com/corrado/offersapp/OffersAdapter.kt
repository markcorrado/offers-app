package com.corrado.offersapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class OffersAdapter(private val myDataset: ArrayList<OfferData>) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.findViewById(R.id.image_view) as ImageView
        val amountTextView = view.findViewById(R.id.amount_text_view) as TextView
        val nameTextView = view.findViewById(R.id.name_text_view) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.offer_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.amountTextView.text = myDataset[position].currentValue
        holder.nameTextView.text = myDataset[position].description
        Picasso.get().load(myDataset[position].url).placeholder(R.drawable.ic_launcher_background).into(holder.imageView)
    }

    override fun getItemCount() = myDataset.size
}