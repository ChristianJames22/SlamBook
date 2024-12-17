package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie, Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movies, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onItemClick(movie, position) }
    }

    override fun getItemCount() = movies.size

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
        private val genreTextView: TextView = view.findViewById(R.id.textViewGenre)
        private val yearTextView: TextView = view.findViewById(R.id.textViewYear)
        private val directorTextView: TextView = view.findViewById(R.id.textViewDirector)

        fun bind(movie: Movie) {
            titleTextView.text = "Title: ${movie.title}"
            genreTextView.text = "Genre(s): ${movie.genre}"
            yearTextView.text = "Release Year: ${ movie.releaseYear}"
            directorTextView.text = "Laguage: ${movie.director}"
        }
    }
}
