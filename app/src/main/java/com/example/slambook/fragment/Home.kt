package com.example.slambook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)

        // Update to CardView
        val btnMovie: CardView = view.findViewById(R.id.btnMovie)
        val btnSongs: CardView = view.findViewById(R.id.btnSongs)
        val btnBook: CardView = view.findViewById(R.id.btnBook)
        val btnPlace: CardView = view.findViewById(R.id.btnPlaces)
        val btnFood: CardView = view.findViewById(R.id.btnFood)
        val btnDrink: CardView = view.findViewById(R.id.btnDrinks)

        // Movie Button Click
        btnMovie.setOnClickListener {
            val intent = Intent(requireContext(), MovieActivity::class.java)
            startActivity(intent)
        }

        // Songs Button Click
        btnSongs.setOnClickListener {
            val intent = Intent(requireContext(), SongsActivity::class.java)
            startActivity(intent)
        }

        // Book Button Click
        btnBook.setOnClickListener {
            val intent = Intent(requireContext(), BookActivity::class.java)
            startActivity(intent)
        }

        // Place Button Click
        btnPlace.setOnClickListener {
            val intent = Intent(requireContext(), PlaceActivity::class.java)
            startActivity(intent)
        }

        // Food Button Click
        btnFood.setOnClickListener {
            val intent = Intent(requireContext(), FoodActivity::class.java)
            startActivity(intent)
        }

        // Drink Button Click
        btnDrink.setOnClickListener {
            val intent = Intent(requireContext(), DrinkActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
