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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.model.enums.TaskState
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityTaskListBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.adapter.TaskListAdapter
import com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.*

class TaskListActivity : AppCompatActivity() {
    private lateinit var taskListBinding: ActivityTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()

    private var taskListAdapter: TaskListAdapter? = null
    private var taskList: ArrayList<Task>? = null

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
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_blue)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

        taskListBinding.srlRefreshTaskList.isRefreshing = true
        taskViewModel.getAllTasks()

        setUpObservers()
        setupOnClickListeners()
        setupOnRefreshListeners()
        setSwipeToDelete()
    }

    private fun setSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                taskList?.let { notNullTaskList ->
                    val position = viewHolder.adapterPosition
                    val task = notNullTaskList[position]

                    notNullTaskList.removeAt(position)
                    taskListAdapter?.notifyItemRemoved(position)
                    updateTaskListUI()

                    Snackbar.make(
                        taskListBinding.rvTaskList,
                        "Se ha eliminado la tarea ${task.title}",
                        Snackbar.LENGTH_LONG
                    ).setAction("Undo") {
                        notNullTaskList.add(position, task)
                        taskListAdapter?.notifyItemInserted(position)
                        updateTaskListUI()
                    }.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != DISMISS_EVENT_ACTION) {
                                task.id?.let { taskViewModel.deleteTaskById(it) }
                            }
                        }
                    }).show()

                }
            }

        }).attachToRecyclerView(taskListBinding.rvTaskList)
    }

    private fun setupOnRefreshListeners() {
        taskListBinding.srlRefreshTaskList.setOnRefreshListener {
            taskViewModel.getAllTasks()
        }
    }

    private fun setupOnClickListeners() {
        taskListBinding.btnAddTask.setOnClickListener {
            startActivity(Intent(this, UpdateTaskActivity::class.java))
        }
    }

    private fun setUpObservers() {
        taskViewModel.allTasks.observe(this) { allTasks ->
            taskList = allTasks
            updateTaskListUI()
            if (allTasks.isNotEmpty()) {
                taskListAdapter =
                    TaskListAdapter(allTasks,
                        onClickTaskCompletedListener = { task ->
                            onClickTaskCompletedListener(task)
                        },
                        onClickItemListener = { task ->
                            onClickItemListener(task)
                        })

                taskListBinding.rvTaskList.layoutManager = LinearLayoutManager(this)
                taskListBinding.rvTaskList.adapter = taskListAdapter
            }

            taskListBinding.srlRefreshTaskList.isRefreshing = false
        }
    }

    private fun onClickItemListener(task: Task) {
        val intent = Intent(this, UpdateTaskActivity::class.java)
        intent.putExtra("TASK_ID", task.id)
        startActivity(intent)
    }

    private fun onClickTaskCompletedListener(task: Task) {
        taskViewModel.saveTask(task)
    }

    private fun updateTaskListUI() {
        if (taskList.isNullOrEmpty()) {
            taskListBinding.rvTaskList.visibility = View.GONE
            taskListBinding.tvCreateATask.visibility = View.VISIBLE
        } else {
            taskListBinding.rvTaskList.visibility = View.VISIBLE
            taskListBinding.tvCreateATask.visibility = View.GONE
        }
    }

}