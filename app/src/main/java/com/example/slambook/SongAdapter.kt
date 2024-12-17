package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private val songs: List<Song>,
    private val onItemClick: (Song, Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_songs, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
        holder.itemView.setOnClickListener { onItemClick(song, position) }
    }

    override fun getItemCount() = songs.size

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
        private val genreTextView: TextView = view.findViewById(R.id.textViewGenre)
        private val releaseYearTextView: TextView = view.findViewById(R.id.textViewReleaseYear)
        private val artistTextView: TextView = view.findViewById(R.id.textViewArtist)

        fun bind(song: Song) {
            titleTextView.text = "Title: ${song.title}"
            genreTextView.text = "Genre(s): ${song.genre}"
            releaseYearTextView.text = "Release Year: ${song.releaseYear}"
            artistTextView.text = "Artist(s): ${song.artist}"
        }
    }
}
