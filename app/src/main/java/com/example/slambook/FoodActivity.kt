package com.example.slambook

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    private val foodList = mutableListOf<Food>()
    private val sharedPreferences by lazy {
        getSharedPreferences("foods_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        recyclerView = findViewById(R.id.recyclerViewFoods)
        val fabAddFood: FloatingActionButton = findViewById(R.id.fabAddFood)
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        loadFoods()

        foodAdapter = FoodAdapter(foodList) { food, position -> onFoodClicked(food, position) }
        recyclerView.adapter = foodAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddFood.setOnClickListener { showAddFoodDialog() }
        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }

    private fun showAddFoodDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_food, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val cuisineEditText: EditText = dialogView.findViewById(R.id.editTextCuisine)
        val ingredientsEditText: EditText = dialogView.findViewById(R.id.editTextIngredients)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)

        AlertDialog.Builder(this)
            .setTitle("Add Food")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEditText.text.toString()
                val cuisine = cuisineEditText.text.toString()
                val ingredients = ingredientsEditText.text.toString()
                val type = typeEditText.text.toString()
                if (name.isNotEmpty() && cuisine.isNotEmpty() && ingredients.isNotEmpty() && type.isNotEmpty()) {
                    val newFood = Food(name, cuisine, ingredients, type)
                    foodList.add(0, newFood)
                    foodAdapter.notifyDataSetChanged()
                    saveFoods()
                    Toast.makeText(this, "Food added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onFoodClicked(food: Food, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Food Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateFoodDialog(food, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdateFoodDialog(food: Food, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_food, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val cuisineEditText: EditText = dialogView.findViewById(R.id.editTextCuisine)
        val ingredientsEditText: EditText = dialogView.findViewById(R.id.editTextIngredients)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)

        nameEditText.setText(food.name)
        cuisineEditText.setText(food.cuisine)
        ingredientsEditText.setText(food.ingredients)
        typeEditText.setText(food.type)

        AlertDialog.Builder(this)
            .setTitle("Update Food")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = nameEditText.text.toString()
                val cuisine = cuisineEditText.text.toString()
                val ingredients = ingredientsEditText.text.toString()
                val type = typeEditText.text.toString()
                if (name.isNotEmpty() && cuisine.isNotEmpty() && ingredients.isNotEmpty() && type.isNotEmpty()) {
                    foodList.removeAt(position)
                    val updatedFood = Food(name, cuisine, ingredients, type)
                    foodList.add(0, updatedFood)
                    foodAdapter.notifyDataSetChanged()
                    saveFoods()
                    Toast.makeText(this, "Food updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Food")
            .setMessage("Are you sure you want to delete this food?")
            .setPositiveButton("Yes") { _, _ ->
                foodList.removeAt(position)
                foodAdapter.notifyItemRemoved(position)
                saveFoods()
                Toast.makeText(this, "Food deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveFoods() {
        val gson = Gson()
        val json = gson.toJson(foodList)
        sharedPreferences.edit().putString("food_list", json).apply()
    }

    private fun loadFoods() {
        val gson = Gson()
        val json = sharedPreferences.getString("food_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Food>>() {}.type
            val savedFoods: MutableList<Food> = gson.fromJson(json, type)
            foodList.clear()
            foodList.addAll(savedFoods)
        }
    }
}
