package com.rangeljhoandev.todolist_kotlin_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.databinding.ItemTaskListBinding
import com.rangeljhoandev.todolist_kotlin_app.util.DateUtil

class TaskListAdapter(private val taskList: ArrayList<Task>) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    inner class ViewHolder(private val binding: ItemTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.tvTaskDescription.text = task.description

            val context = itemView.context
            val dateFormatted = DateUtil.formatDate2String(task.dueDate, "dd-MM-yyyy HH:mm")
            binding.tvTaskDueDate.text = context.getString(R.string.concat_due_date, dateFormatted)

        }
    }
}