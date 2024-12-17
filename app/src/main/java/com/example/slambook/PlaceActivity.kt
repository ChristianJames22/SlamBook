package com.example.slambook

import android.annotation.SuppressLint
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

class PlaceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private val placeList = mutableListOf<Place>()
    private val sharedPreferences by lazy {
        getSharedPreferences("places_pref", Context.MODE_PRIVATE)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        recyclerView = findViewById(R.id.recyclerViewPlaces)
        val fabAddPlace: FloatingActionButton = findViewById(R.id.fabAddPlace)
        val btnBack: ImageButton = findViewById(R.id.btnBack)


        loadPlaces()

        placeAdapter = PlaceAdapter(placeList) { place, position -> onPlaceClicked(place, position) }
        recyclerView.adapter = placeAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddPlace.setOnClickListener { showAddPlaceDialog() }
        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }

    private fun showAddPlaceDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_place, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)
        val addressEditText: EditText = dialogView.findViewById(R.id.editTextAddress)
        val countryEditText: EditText = dialogView.findViewById(R.id.editTextCountry)

        AlertDialog.Builder(this)
            .setTitle("Add Place")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEditText.text.toString()
                val type = typeEditText.text.toString()
                val address = addressEditText.text.toString()
                val country = countryEditText.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty() && address.isNotEmpty() && country.isNotEmpty()) {
                    val newPlace = Place(name, type, address, country)
                    placeList.add(0, newPlace)
                    placeAdapter.notifyDataSetChanged()
                    savePlaces()
                    Toast.makeText(this, "Place added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onPlaceClicked(place: Place, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Place Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdatePlaceDialog(place, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdatePlaceDialog(place: Place, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_place, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        val typeEditText: EditText = dialogView.findViewById(R.id.editTextType)
        val addressEditText: EditText = dialogView.findViewById(R.id.editTextAddress)
        val countryEditText: EditText = dialogView.findViewById(R.id.editTextCountry)

        nameEditText.setText(place.name)
        typeEditText.setText(place.type)
        addressEditText.setText(place.address)
        countryEditText.setText(place.country)

        AlertDialog.Builder(this)
            .setTitle("Update Place")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = nameEditText.text.toString()
                val type = typeEditText.text.toString()
                val address = addressEditText.text.toString()
                val country = countryEditText.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty() && address.isNotEmpty() && country.isNotEmpty()) {
                    placeList.removeAt(position)
                    val updatedPlace = Place(name, type, address, country)
                    placeList.add(0, updatedPlace)
                    placeAdapter.notifyDataSetChanged()
                    savePlaces()
                    Toast.makeText(this, "Place updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Place")
            .setMessage("Are you sure you want to delete this place?")
            .setPositiveButton("Yes") { _, _ ->
                placeList.removeAt(position)
                placeAdapter.notifyItemRemoved(position)
                savePlaces()
                Toast.makeText(this, "Place deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun savePlaces() {
        val gson = Gson()
        val json = gson.toJson(placeList)
        sharedPreferences.edit().putString("place_list", json).apply()
    }

    private fun loadPlaces() {
        val gson = Gson()
        val json = sharedPreferences.getString("place_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Place>>() {}.type
            val savedPlaces: MutableList<Place> = gson.fromJson(json, type)
            placeList.clear()
            placeList.addAll(savedPlaces)
        }
    }
}
