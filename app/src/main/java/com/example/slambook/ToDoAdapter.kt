package com.example.slambook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(
    private val toDoList: MutableList<Pair<String, String>>,
    private val onDeleteClick: (Int) -> Unit,
    private val onUpdateClick: (Int) -> Unit
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val task = toDoList[position]
        holder.bind(task)
        holder.deleteButton.setOnClickListener { onDeleteClick(position) }
        holder.updateButton.setOnClickListener { onUpdateClick(position) }
    }

    override fun getItemCount() = toDoList.size

    class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskTextView: TextView = view.findViewById(R.id.tvToDoItem)
        private val dateTimeTextView: TextView = view.findViewById(R.id.tvTimestamp)
        val deleteButton: Button = view.findViewById(R.id.btnDelete)
        val updateButton: Button = view.findViewById(R.id.btnUpdate)

        fun bind(task: Pair<String, String>) {
            taskTextView.text = "Task: ${task.first}" // Display the task
            dateTimeTextView.text = "Added on \n${task.second.replace(" ", "\n")}" // Separate date and time
        }
    }
}
