package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.domain.DeleteTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetAllTasksUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.SaveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase
) : BaseViewModel() {

    val allTasks = MutableLiveData<ArrayList<Task>>()
    val savedTask = MutableLiveData<Task?>()
    val taskById = MutableLiveData<Task?>()
    val deletedTask = MutableLiveData<Task?>()

    fun getAllTasks() {
        executeUseCase({ getAllTasksUseCase() }, allTasks)
    }

    fun getTaskById(taskId: Long) {
        val response = suspend { getTaskByIdUseCase(taskId) }
        executeUseCase(response, taskById)
    }

    fun saveTask(task: Task) {
        executeUseCase({ saveTaskUseCase(task) }, savedTask)
    }

    fun deleteTaskById(idTask: Long) {
        executeUseCase({ deleteTaskByIdUseCase(idTask) }, deletedTask)
    }

}