package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DrinkAdapter(
    private val drinks: List<Drink>,
    private val onItemClick: (Drink, Int) -> Unit
) : RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drinks, parent, false)
        return DrinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drink = drinks[position]
        holder.bind(drink)
        holder.itemView.setOnClickListener { onItemClick(drink, position) }
    }

    override fun getItemCount() = drinks.size

    class DrinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.textViewName)
        private val typeTextView: TextView = view.findViewById(R.id.textViewType)
        private val ingredientsTextView: TextView = view.findViewById(R.id.textViewIngredients)
        private val servingSizeTextView: TextView = view.findViewById(R.id.textViewServingSize)

        fun bind(drink: Drink) {
            nameTextView.text = "Name: ${drink.name}"
            typeTextView.text = "Category: ${drink.type}"
            ingredientsTextView.text = "Flavor: ${drink.ingredients}"
            servingSizeTextView.text = "Serving Size: ${drink.servingSize}"
        }
    }
}
