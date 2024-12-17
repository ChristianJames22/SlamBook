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

class BookActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private val bookList = mutableListOf<Book>()
    private val sharedPreferences by lazy {
        getSharedPreferences("books_pref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        recyclerView = findViewById(R.id.recyclerViewBooks)
        val fabAddBook: FloatingActionButton = findViewById(R.id.fabAddBook)
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        loadBooks()

        bookAdapter = BookAdapter(bookList) { book, position -> onBookClicked(book, position) }
        recyclerView.adapter = bookAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddBook.setOnClickListener { showAddBookDialog() }

        btnBack.setOnClickListener {
            finish() // End the activity and return to the previous screen
        }
    }

    private fun showAddBookDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val authorEditText: EditText = dialogView.findViewById(R.id.editTextAuthor)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val publicationDateEditText: EditText = dialogView.findViewById(R.id.editTextPublicationDate)

        AlertDialog.Builder(this)
            .setTitle("Add Book")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleEditText.text.toString()
                val author = authorEditText.text.toString()
                val genre = genreEditText.text.toString()
                val publicationDate = publicationDateEditText.text.toString()
                if (title.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty() && publicationDate.isNotEmpty()) {
                    val newBook = Book(title, author, genre, publicationDate)
                    bookList.add(0, newBook)
                    bookAdapter.notifyDataSetChanged()
                    saveBooks()
                    Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun onBookClicked(book: Book, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Book Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateBookDialog(book, position)
                    1 -> showDeleteConfirmationDialog(position)
                }
            }
            .show()
    }

    private fun showUpdateBookDialog(book: Book, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val authorEditText: EditText = dialogView.findViewById(R.id.editTextAuthor)
        val genreEditText: EditText = dialogView.findViewById(R.id.editTextGenre)
        val publicationDateEditText: EditText = dialogView.findViewById(R.id.editTextPublicationDate)

        titleEditText.setText(book.title)
        authorEditText.setText(book.author)
        genreEditText.setText(book.genre)
        publicationDateEditText.setText(book.publicationDate)

        AlertDialog.Builder(this)
            .setTitle("Update Book")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val title = titleEditText.text.toString()
                val author = authorEditText.text.toString()
                val genre = genreEditText.text.toString()
                val publicationDate = publicationDateEditText.text.toString()
                if (title.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty() && publicationDate.isNotEmpty()) {
                    bookList.removeAt(position)
                    val updatedBook = Book(title, author, genre, publicationDate)
                    bookList.add(0, updatedBook)
                    bookAdapter.notifyDataSetChanged()
                    saveBooks()
                    Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete this book?")
            .setPositiveButton("Yes") { _, _ ->
                bookList.removeAt(position)
                bookAdapter.notifyItemRemoved(position)
                saveBooks()
                Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun saveBooks() {
        val gson = Gson()
        val json = gson.toJson(bookList)
        sharedPreferences.edit().putString("book_list", json).apply()
    }

    private fun loadBooks() {
        val gson = Gson()
        val json = sharedPreferences.getString("book_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Book>>() {}.type
            val savedBooks: MutableList<Book> = gson.fromJson(json, type)
            bookList.clear()
            bookList.addAll(savedBooks)
        }
    }
}
