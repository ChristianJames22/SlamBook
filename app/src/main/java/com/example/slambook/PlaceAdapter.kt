package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(
    private val places: List<Place>,
    private val onItemClick: (Place, Int) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_places, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
        holder.itemView.setOnClickListener { onItemClick(place, position) }
    }

    override fun getItemCount() = places.size

    class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.textViewName)
        private val typeTextView: TextView = view.findViewById(R.id.textViewType)
        private val addressTextView: TextView = view.findViewById(R.id.textViewAddress)
        private val countryTextView: TextView = view.findViewById(R.id.textViewCountry)

        fun bind(place: Place) {
            nameTextView.text = "Name: ${place.name}"
            typeTextView.text ="Type: ${place.type}"
            addressTextView.text = "Address: ${place.address}"
            countryTextView.text = "Category: ${place.country}"
        }
    }
}
