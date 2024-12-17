package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val foods: List<Food>,
    private val onItemClick: (Food, Int) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_foods, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)
        holder.itemView.setOnClickListener { onItemClick(food, position) }
    }

    override fun getItemCount() = foods.size

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.textViewName)
        private val cuisineTextView: TextView = view.findViewById(R.id.textViewCuisine)
        private val ingredientsTextView: TextView = view.findViewById(R.id.textViewIngredients)
        private val typeTextView: TextView = view.findViewById(R.id.textViewType)

        fun bind(food: Food) {
            nameTextView.text = "Name: ${food.name}"
            cuisineTextView.text = "Cussine: ${food.cuisine}"
            ingredientsTextView.text = "Category: ${food.ingredients}"
            typeTextView.text = "Description: ${food.type}"
        }
    }
}
