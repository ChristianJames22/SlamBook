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

class MovieActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()
    private val sharedPreferences by lazy {
        getSharedPreferences("movies_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        recyclerView = findViewById(R.id.recyclerViewMovies)
        val fabAddMovie: FloatingActionButton = findViewById(R.id.fabAddMovie)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        loadMovies()

        movieAdapter = MovieAdapter(movieList) { movie, position -> onMovieClicked(movie, position) }
        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddMovie.setOnClickListener { showAddMovieDialog() }
        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }

    private fun showAddMovieDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_movie, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val releaseYearEditText: EditText = dialogView.findViewById(R.id.editTextYear)
        val directorEditText: EditText = dialogView.findViewById(R.id.editTextDirector)

        AlertDialog.Builder(this)
            .setTitle("Add Movie")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val genre = genreEditText.text.toString()
                val releaseYear = releaseYearEditText.text.toString()
                val director = directorEditText.text.toString()
                if (title.isNotEmpty() && genre.isNotEmpty() && releaseYear.isNotEmpty() && director.isNotEmpty()) {
                    val newMovie = Movie(title, genre, releaseYear, director)
                    movieList.add(0, newMovie)
                    movieAdapter.notifyDataSetChanged()
                    saveMovies()
                    Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onMovieClicked(movie: Movie, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Movie Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateMovieDialog(movie, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdateMovieDialog(movie: Movie, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_movie, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val releaseYearEditText: EditText = dialogView.findViewById(R.id.editTextYear)
        val directorEditText: EditText = dialogView.findViewById(R.id.editTextDirector)

        titleEditText.setText(movie.title)
        genreEditText.setText(movie.genre)
        releaseYearEditText.setText(movie.releaseYear)
        directorEditText.setText(movie.director)

        AlertDialog.Builder(this)
            .setTitle("Update Movie")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val title = titleEditText.text.toString()
                val genre = genreEditText.text.toString()
                val releaseYear = releaseYearEditText.text.toString()
                val director = directorEditText.text.toString()
                if (title.isNotEmpty() && genre.isNotEmpty() && releaseYear.isNotEmpty() && director.isNotEmpty()) {
                    movieList.removeAt(position)
                    val updatedMovie = Movie(title, genre, releaseYear, director)
                    movieList.add(0, updatedMovie)
                    movieAdapter.notifyDataSetChanged()
                    saveMovies()
                    Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Movie")
            .setMessage("Are you sure you want to delete this movie?")
            .setPositiveButton("Yes") { _, _ ->
                movieList.removeAt(position)
                movieAdapter.notifyItemRemoved(position)
                saveMovies()
                Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveMovies() {
        val gson = Gson()
        val json = gson.toJson(movieList)
        sharedPreferences.edit().putString("movie_list", json).apply()
    }

    private fun loadMovies() {
        val gson = Gson()
        val json = sharedPreferences.getString("movie_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Movie>>() {}.type
            val savedMovies: MutableList<Movie> = gson.fromJson(json, type)
            movieList.clear()
            movieList.addAll(savedMovies)
        }
    }
}
