package com.rangeljhoandev.todolist_kotlin_app.ui.view

import android.content.Intent
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
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityUpdateTaskBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.view.fragment.DatePickerFragment
import com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel.TaskViewModel
import com.rangeljhoandev.todolist_kotlin_app.util.DateUtil
import java.util.Calendar
import java.util.Date

class UpdateTaskActivity : AppCompatActivity() {
    private lateinit var updateTaskBinding: ActivityUpdateTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()

    private var selectedDueDate: Date? = null
    private var taskId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTaskBinding = ActivityUpdateTaskBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(updateTaskBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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

        if (intent.hasExtra("TASK_ID")) {
            taskId = intent.getLongExtra("TASK_ID", 0)
            taskViewModel.getTaskById(taskId!!)
        }

        setupOnClickListeners()
        setupObservers()
    }

    private fun setupObservers() {
        taskViewModel.taskById.observe(this) { task ->
            task?.let {
                updateTaskBinding.etTaskTitle.setText(task.title)
                updateTaskBinding.etTaskDescription.setText(task.description)

                selectedDueDate = task.dueDate
                updateTaskBinding.etTaskDueDate.setText(
                    DateUtil.formatDate2String(
                        task.dueDate,
                        "dd-MM-yyyy 'at' HH:mm"
                    )
                )
            }
        }

        taskViewModel.savedTask.observe(this) { savedTask ->
            if (savedTask != null) {
                Toast.makeText(this, "The task was saved", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, TaskListActivity::class.java))
            } else {
                Toast.makeText(this, "The task was not saved", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupOnClickListeners() {
        updateTaskBinding.etTaskDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        updateTaskBinding.btnCreateTask.setOnClickListener {
            val title = updateTaskBinding.etTaskTitle.text.toString()
            val description = updateTaskBinding.etTaskDescription.text.toString()

            if (selectedDueDate != null) {
                val task = Task(taskId, title, description, Date(), selectedDueDate!!, 1)
                taskViewModel.saveTask(task)
            }
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

        selectedDueDate = c.time

        updateTaskBinding.etTaskDueDate.setText(
            DateUtil.formatDate2String(
                c.time,
                "dd-MM-yyyy 'at' HH:mm"
            )
        )
    }
}