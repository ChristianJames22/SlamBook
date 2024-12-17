package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val books: List<Book>,
    private val onItemClick: (Book, Int) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_books, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
        holder.itemView.setOnClickListener { onItemClick(book, position) }
    }

    override fun getItemCount() = books.size

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
        private val authorTextView: TextView = view.findViewById(R.id.textViewAuthor)
        private val genreTextView: TextView = view.findViewById(R.id.textViewGenre)
        private val publicationDateTextView: TextView = view.findViewById(R.id.textViewPublicationDate)

        fun bind(book: Book) {
            titleTextView.text = "Title: ${book.title}"
            authorTextView.text = "Author(s): ${book.author}"
            genreTextView.text = "Gnere(s): ${book.genre}"
            publicationDateTextView.text = "Publication Year: ${book.publicationDate}"
        }
    }
}
