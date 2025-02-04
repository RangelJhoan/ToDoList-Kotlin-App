package com.rangeljhoandev.todolist_kotlin_app.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityTaskListBinding
import com.rangeljhoandev.todolist_kotlin_app.ui.adapter.TaskListAdapter
import com.rangeljhoandev.todolist_kotlin_app.ui.view.constants.TaskKeys
import com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {
    private lateinit var taskListBinding: ActivityTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()

    private lateinit var taskListAdapter: TaskListAdapter
    private val taskList: ArrayList<Task> = arrayListOf()

    private val updateTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                taskViewModel.getAllTasks()
            }
        }

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

        setupObservers()
        setupAdapters()
        setupOnClickListeners()
        setupOnRefreshListeners()
        setSwipeToDelete()
    }

    private fun setupAdapters() {
        taskListAdapter =
            TaskListAdapter(
                taskList,
                onClickTaskCompletedListener = { task ->
                    onClickTaskCompletedListener(task)
                },
                onClickItemListener = { task ->
                    onClickItemListener(task)
                })

        taskListBinding.rvTaskList.layoutManager = LinearLayoutManager(this)
        taskListBinding.rvTaskList.adapter = taskListAdapter
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
                taskListBinding.srlRefreshTaskList.isEnabled = false

                val position = viewHolder.adapterPosition
                val task = taskList[position]

                taskList.removeAt(position)
                taskListAdapter.notifyItemRemoved(position)
                updateTaskListUI()

                Snackbar.make(
                    taskListBinding.rvTaskList,
                    "Task ${task.title} was deleted",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    taskList.add(position, task)
                    taskListAdapter.notifyItemInserted(position)
                    updateTaskListUI()
                    taskListBinding.srlRefreshTaskList.isEnabled = true
                }.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event != DISMISS_EVENT_ACTION) {
                            task.id?.let { taskViewModel.deleteTaskById(it) }
                            taskListBinding.srlRefreshTaskList.isEnabled = true
                        }
                    }
                }).show()
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
            val intent = Intent(this, UpdateTaskActivity::class.java)
            updateTaskLauncher.launch(intent)
        }
    }

    private fun setupObservers() {
        taskViewModel.allTasks.observe(this) { allTasks ->
            taskListAdapter.updateTasks(allTasks)
            updateTaskListUI()
        }

        taskViewModel.errorMessage.observe(this) { errorMessage ->
            taskList.clear()
            updateTaskListUI()
            taskListBinding.tvTaskListMessage.text = errorMessage
        }
    }

    private fun onClickItemListener(task: Task) {
        val intent = Intent(this, UpdateTaskActivity::class.java).apply {
            putExtra(TaskKeys.TASK_ID, task.id)
        }
        updateTaskLauncher.launch(intent)
    }

    private fun onClickTaskCompletedListener(task: Task) {
        taskViewModel.saveTask(task)
    }

    private fun updateTaskListUI() {
        if (taskList.isEmpty()) {
            taskListBinding.rvTaskList.visibility = View.GONE
            taskListBinding.tvTaskListMessage.visibility = View.VISIBLE
            taskListBinding.tvTaskListMessage.text = getString(R.string.no_tasks_created)
        } else {
            taskListBinding.rvTaskList.visibility = View.VISIBLE
            taskListBinding.tvTaskListMessage.visibility = View.GONE
        }
        taskListBinding.srlRefreshTaskList.isRefreshing = false
    }

}
