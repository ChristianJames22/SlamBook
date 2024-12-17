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

class DrinkActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var drinkAdapter: DrinkAdapter
    private val drinkList = mutableListOf<Drink>()
    private val sharedPreferences by lazy {
        getSharedPreferences("drinks_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)

        recyclerView = findViewById(R.id.recyclerViewDrinks)
        val fabAddDrink: FloatingActionButton = findViewById(R.id.fabAddDrink)
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        loadDrinks()

        drinkAdapter = DrinkAdapter(drinkList) { drink, position -> onDrinkClicked(drink, position) }
        recyclerView.adapter = drinkAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddDrink.setOnClickListener { showAddDrinkDialog() }
        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }

    private fun showAddDrinkDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_drink, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)
        val ingredientsEditText: EditText = dialogView.findViewById(R.id.editTextIngredients)
        val servingSizeEditText: EditText = dialogView.findViewById(R.id.editTextServingSize)

        AlertDialog.Builder(this)
            .setTitle("Add Drink")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEditText.text.toString()
                val type = typeEditText.text.toString()
                val ingredients = ingredientsEditText.text.toString()
                val servingSize = servingSizeEditText.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty() && ingredients.isNotEmpty() && servingSize.isNotEmpty()) {
                    val newDrink = Drink(name, type, ingredients, servingSize)
                    drinkList.add(0, newDrink)
                    drinkAdapter.notifyDataSetChanged()
                    saveDrinks()
                    Toast.makeText(this, "Drink added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onDrinkClicked(drink: Drink, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Drink Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateDrinkDialog(drink, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdateDrinkDialog(drink: Drink, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_drink, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)
        val ingredientsEditText: EditText = dialogView.findViewById(R.id.editTextIngredients)
        val servingSizeEditText: EditText = dialogView.findViewById(R.id.editTextServingSize)

        nameEditText.setText(drink.name)
        typeEditText.setText(drink.type)
        ingredientsEditText.setText(drink.ingredients)
        servingSizeEditText.setText(drink.servingSize)

        AlertDialog.Builder(this)
            .setTitle("Update Drink")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = nameEditText.text.toString()
                val type = typeEditText.text.toString()
                val ingredients = ingredientsEditText.text.toString()
                val servingSize = servingSizeEditText.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty() && ingredients.isNotEmpty() && servingSize.isNotEmpty()) {
                    drinkList.removeAt(position)
                    val updatedDrink = Drink(name, type, ingredients, servingSize)
                    drinkList.add(0, updatedDrink)
                    drinkAdapter.notifyDataSetChanged()
                    saveDrinks()
                    Toast.makeText(this, "Drink updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Drink")
            .setMessage("Are you sure you want to delete this drink?")
            .setPositiveButton("Yes") { _, _ ->
                drinkList.removeAt(position)
                drinkAdapter.notifyItemRemoved(position)
                saveDrinks()
                Toast.makeText(this, "Drink deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveDrinks() {
        val gson = Gson()
        val json = gson.toJson(drinkList)
        sharedPreferences.edit().putString("drink_list", json).apply()
    }

    private fun loadDrinks() {
        val gson = Gson()
        val json = sharedPreferences.getString("drink_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Drink>>() {}.type
            val savedDrinks: MutableList<Drink> = gson.fromJson(json, type)
            drinkList.clear()
            drinkList.addAll(savedDrinks)
        }
    }
}
