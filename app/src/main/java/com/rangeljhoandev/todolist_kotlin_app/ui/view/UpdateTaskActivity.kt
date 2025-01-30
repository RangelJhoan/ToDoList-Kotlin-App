package com.rangeljhoandev.todolist_kotlin_app.ui.view

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityUpdateTaskBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.view.constants.TaskKeys
import com.rangeljhoandev.todolist_kotlin_app.ui.view.fragment.DatePickerFragment
import com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel.TaskViewModel
import com.rangeljhoandev.todolist_kotlin_app.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class UpdateTaskActivity : AppCompatActivity() {
    private lateinit var updateTaskBinding: ActivityUpdateTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()

    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTaskBinding = ActivityUpdateTaskBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(updateTaskBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(updateTaskBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_blue)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

        if (intent.hasExtra(TaskKeys.TASK_ID)) {
            task = Task(intent.getLongExtra(TaskKeys.TASK_ID, 0), "", "", Date(), Date(), -1)
            taskViewModel.getTaskById(task!!.id!!)
        }

        setupOnClickListeners()
        setupObservers()
    }

    private fun setupObservers() {
        taskViewModel.taskById.observe(this) { taskById ->
            if (taskById == null) {
                Toast.makeText(this, "Task was not found", Toast.LENGTH_LONG).show()
                finish()
                return@observe
            }

            task = taskById
            updateTaskBinding.etTaskTitle.setText(taskById.title)
            updateTaskBinding.etTaskDescription.setText(taskById.description)

            updateTaskBinding.etTaskDueDate.setText(
                DateUtil.formatDate2String(taskById.dueDate, "dd-MM-yyyy 'at' HH:mm")
            )
        }

        taskViewModel.savedTask.observe(this) { savedTask ->
            if (savedTask != null) {
                Toast.makeText(this, "Task was updated", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Error updating task", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validInputs(): Boolean {
        var isValid = true

        val title = updateTaskBinding.etTaskTitle.text.toString()
        if (title.isBlank()) {
            updateTaskBinding.tilTaskTitle.error = "Title is required"
            isValid = false
        } else {
            updateTaskBinding.tilTaskTitle.error = null
        }

        val description = updateTaskBinding.etTaskDescription.text.toString()
        if (description.isBlank()) {
            updateTaskBinding.tilTaskDescription.error = "Description is required"
            isValid = false
        } else {
            updateTaskBinding.tilTaskDescription.error = null
        }

        if (task?.dueDate == null) {
            updateTaskBinding.tilTaskDueDate.error = "Due date is required"
            updateTaskBinding.tilTaskDueDate.errorIconDrawable = null
            updateTaskBinding.tilTaskDueDate.endIconMode = TextInputLayout.END_ICON_CUSTOM
            updateTaskBinding.tilTaskDueDate.setEndIconDrawable(R.drawable.ic_calendar)

            val errorColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
            updateTaskBinding.tilTaskDueDate.setEndIconTintList(errorColor)

            isValid = false
        } else {
            updateTaskBinding.tilTaskDueDate.error = null
            val normalColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))
            updateTaskBinding.tilTaskDueDate.setEndIconTintList(normalColor)
        }

        return isValid
    }

    private fun setupOnClickListeners() {
        updateTaskBinding.etTaskDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        updateTaskBinding.btnCreateTask.setOnClickListener {
            if (!validInputs()) return@setOnClickListener

            val taskId = task?.id
            val taskState = if (task?.state?.toInt() != -1) task?.state ?: 1 else 1
            val taskCreationDate = task?.creationDate ?: Date()

            val title = updateTaskBinding.etTaskTitle.text.toString()
            val description = updateTaskBinding.etTaskDescription.text.toString()

            val task =
                Task(taskId, title, description, taskCreationDate, task?.dueDate!!, taskState)
            taskViewModel.saveTask(task)
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year, hour, minute ->
            onDateSelected(
                day,
                month,
                year,
                hour,
                minute
            )
        }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int, hour: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute, 0)

        if (task == null) {
            task = Task(null, "", "", Date(), Date(), -1)
        }

        task = task?.copy(dueDate = c.time)?.apply {
            updateTaskBinding.etTaskDueDate.setText(
                DateUtil.formatDate2String(
                    dueDate,
                    "dd-MM-yyyy 'at' HH:mm"
                )
            )
        }

    }
}