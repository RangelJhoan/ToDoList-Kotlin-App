package com.rangeljhoandev.todolist_kotlin_app.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityTaskListBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.adapter.TaskListAdapter
import com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel.TaskViewModel

class TaskListActivity : AppCompatActivity() {
    private lateinit var taskListBinding: ActivityTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskListBinding = ActivityTaskListBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(taskListBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(taskListBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        taskViewModel.allTasks.observe(this) { allTasks ->
            val taskListAdapter = TaskListAdapter(allTasks)
            taskListBinding.rvTaskList.layoutManager = LinearLayoutManager(this)
            taskListBinding.rvTaskList.adapter = taskListAdapter
        }
    }
}