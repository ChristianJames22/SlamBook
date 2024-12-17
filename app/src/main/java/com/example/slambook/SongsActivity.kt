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

class SongsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private val songList = mutableListOf<Song>()
    private val sharedPreferences by lazy {
        getSharedPreferences("songs_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        recyclerView = findViewById(R.id.recyclerViewSongs)
        val fabAddSong: FloatingActionButton = findViewById(R.id.fabAddSong)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        loadSongs()

        songAdapter = SongAdapter(songList) { song, position -> onSongClicked(song, position) }
        recyclerView.adapter = songAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddSong.setOnClickListener { showAddSongDialog() }

        // Set up back button click listener
        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }


    private fun showAddSongDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_song, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val releaseYearEditText: EditText = dialogView.findViewById(R.id.editTextReleaseYear)
        val artistEditText: EditText = dialogView.findViewById(R.id.editTextArtist)

        AlertDialog.Builder(this)
            .setTitle("Add Music")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val genre = genreEditText.text.toString()
                val releaseYear = releaseYearEditText.text.toString()
                val artist = artistEditText.text.toString()
                if (title.isNotEmpty() && genre.isNotEmpty() && releaseYear.isNotEmpty() && artist.isNotEmpty()) {
                    val newSong = Song(title, genre, releaseYear, artist)
                    songList.add(0, newSong)
                    songAdapter.notifyDataSetChanged()
                    saveSongs()
                    Toast.makeText(this, "Song added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onSongClicked(song: Song, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Music Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateSongDialog(song, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdateSongDialog(song: Song, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_song, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val releaseYearEditText: EditText = dialogView.findViewById(R.id.editTextReleaseYear)
        val artistEditText: EditText = dialogView.findViewById(R.id.editTextArtist)

        titleEditText.setText(song.title)
        genreEditText.setText(song.genre)
        releaseYearEditText.setText(song.releaseYear)
        artistEditText.setText(song.artist)

        AlertDialog.Builder(this)
            .setTitle("Update Song")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val title = titleEditText.text.toString()
                val genre = genreEditText.text.toString()
                val releaseYear = releaseYearEditText.text.toString()
                val artist = artistEditText.text.toString()
                if (title.isNotEmpty() && genre.isNotEmpty() && releaseYear.isNotEmpty() && artist.isNotEmpty()) {
                    songList.removeAt(position)
                    val updatedSong = Song(title, genre, releaseYear, artist)
                    songList.add(0, updatedSong)
                    songAdapter.notifyDataSetChanged()
                    saveSongs()
                    Toast.makeText(this, "Song updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Song")
            .setMessage("Are you sure you want to delete this song?")
            .setPositiveButton("Yes") { _, _ ->
                songList.removeAt(position)
                songAdapter.notifyItemRemoved(position)
                saveSongs()
                Toast.makeText(this, "Song deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveSongs() {
        val gson = Gson()
        val json = gson.toJson(songList)
        sharedPreferences.edit().putString("song_list", json).apply()
    }

    private fun loadSongs() {
        val gson = Gson()
        val json = sharedPreferences.getString("song_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Song>>() {}.type
            val savedSongs: MutableList<Song> = gson.fromJson(json, type)
            songList.addAll(savedSongs)
        }
    }
}
