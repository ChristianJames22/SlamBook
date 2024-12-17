package com.example.slambook

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ToDo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toDoAdapter: ToDoAdapter
    private val toDoList = mutableListOf<Pair<String, String>>() // Task and DateTime
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_to_do, container, false)

        sharedPreferences = requireContext().getSharedPreferences("ToDoPrefs", Context.MODE_PRIVATE)
        loadTasksFromPreferences()

        recyclerView = view.findViewById(R.id.recyclerViewToDo)
        val addButton: Button = view.findViewById(R.id.btnAddTask)
        val inputEditText: EditText = view.findViewById(R.id.editTextNewTask)

        toDoAdapter = ToDoAdapter(toDoList, { position -> confirmDeleteToDoItem(position) }, { position -> confirmUpdateToDoItem(position) })
        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addButton.setOnClickListener {
            val newTask = inputEditText.text.toString()
            if (newTask.isNotEmpty()) {
                val currentDateTime = getCurrentDateTime()
                confirmAddToDoItem(newTask, currentDateTime, inputEditText)
            } else {
                Toast.makeText(requireContext(), "Task cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun confirmAddToDoItem(task: String, dateTime: String, inputEditText: EditText) {
        val addDialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Add Task")
            .setMessage("Are you sure you want to add this task?")
            .setPositiveButton("Add") { _, _ ->
                addToDoItem(task, dateTime)
                inputEditText.text.clear()
                saveTasksToPreferences()
            }
            .setNegativeButton("Cancel", null)
            .create()
        addDialog.show()
    }

    private fun addToDoItem(task: String, dateTime: String) {
        toDoList.add(0, Pair(task, dateTime)) // Add task with date and time at the top of the list
        toDoAdapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
        saveTasksToPreferences()
    }

    private fun confirmDeleteToDoItem(position: Int) {
        val deleteDialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Complete Task")
            .setMessage("Are you sure you Completed the Task?")
            .setPositiveButton("Competed") { _, _ ->
                deleteToDoItem(position)
            }
            .setNegativeButton("Cancel", null)
            .create()
        deleteDialog.show()
    }

    private fun deleteToDoItem(position: Int) {
        toDoList.removeAt(position)
        toDoAdapter.notifyItemRemoved(position)
        saveTasksToPreferences()
    }

    private fun confirmUpdateToDoItem(position: Int) {
        val currentTask = toDoList[position].first
        val inputEditText = EditText(requireContext())
        inputEditText.setText(currentTask)

        val updateDialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Update Task")
            .setView(inputEditText)
            .setPositiveButton("Update") { _, _ ->
                val updatedTask = inputEditText.text.toString()
                if (updatedTask.isNotEmpty()) {
                    updateToDoItem(position, updatedTask)
                } else {
                    Toast.makeText(requireContext(), "Task cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        updateDialog.show()
    }

    private fun updateToDoItem(position: Int, updatedTask: String) {
        toDoList[position] = Pair(updatedTask, toDoList[position].second) // Keep the original date and time
        toDoAdapter.notifyItemChanged(position)
        saveTasksToPreferences()
    }

    private fun saveTasksToPreferences() {
        val jsonString = gson.toJson(toDoList)
        sharedPreferences.edit().putString("toDoList", jsonString).apply()
    }

    private fun loadTasksFromPreferences() {
        val jsonString = sharedPreferences.getString("toDoList", null)
        if (jsonString != null) {
            val type = object : TypeToken<MutableList<Pair<String, String>>>() {}.type
            val loadedList: MutableList<Pair<String, String>> = gson.fromJson(jsonString, type)
            toDoList.clear()
            toDoList.addAll(loadedList)
        }
    }

    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
