package com.rangeljhoandev.todolist_kotlin_app.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.model.enums.TaskState
import com.rangeljhoandev.todolist_kotlin_app.databinding.ItemTaskListBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.adapter.util.TaskListAdapterDiffUtil
import com.rangeljhoandev.todolist_kotlin_app.util.DateUtil

class TaskListAdapter(
    private val taskList: ArrayList<Task>,
    private val onClickTaskCompletedListener: (task: Task) -> Unit,
    private val onClickItemListener: (task: Task) -> Unit
) :
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

        private val context = itemView.context

        fun bind(task: Task) {
            binding.tvTaskTitle.text = task.title
            binding.tvTaskDescription.text = task.description

            val dateFormatted = DateUtil.formatDate2String(task.dueDate, "dd-MM-yyyy HH:mm")
            binding.tvTaskDueDate.text = context.getString(R.string.concat_due_date, dateFormatted)

            val isTaskChecked = (task.state == TaskState.COMPLETED.id)
            updateUiWhenTaskStateChange(isTaskChecked)
            binding.cbTaskCompleted.isChecked = isTaskChecked

            setupOnClickListeners(task)
        }

        private fun setupOnClickListeners(task: Task) {
            binding.cbTaskCompleted.setOnClickListener {
                val isChecked = binding.cbTaskCompleted.isChecked
                updateUiWhenTaskStateChange(isChecked)
                val updatedTask =
                    task.copy(state = (if (isChecked) TaskState.COMPLETED.id else TaskState.PENDING.id))

                taskList[adapterPosition] = updatedTask
                notifyItemChanged(adapterPosition)
                onClickTaskCompletedListener(updatedTask)
            }

            binding.cvAddNewTask.setOnClickListener {
                onClickItemListener(task)
            }
        }

        private fun updateUiWhenTaskStateChange(isCheckBoxChecked: Boolean) {
            var backgroundColorCvAddNewTask = binding.cvAddNewTask.cardBackgroundColor.defaultColor
            var paintFlagsTvTaskTitle =
                binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            var textColorTvTaskDueDate = ContextCompat.getColor(context, R.color.red)

            if (isCheckBoxChecked) {
                backgroundColorCvAddNewTask = ContextCompat.getColor(context, R.color.light_green)
                paintFlagsTvTaskTitle =
                    binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textColorTvTaskDueDate = ContextCompat.getColor(context, R.color.gray)
            }

            binding.cvAddNewTask.setBackgroundColor(backgroundColorCvAddNewTask)
            binding.tvTaskTitle.paintFlags = paintFlagsTvTaskTitle
            binding.tvTaskDueDate.setTextColor(textColorTvTaskDueDate)
        }
    }

    fun updateTasks(newTasks: ArrayList<Task>) {
        val diffCallback = TaskListAdapterDiffUtil(taskList, newTasks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        taskList.clear()
        taskList.addAll(newTasks)
        diffResult.dispatchUpdatesTo(this)
    }

}