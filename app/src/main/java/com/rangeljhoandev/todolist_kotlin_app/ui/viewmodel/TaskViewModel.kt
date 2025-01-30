package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val savedTaskUseCase: SaveTaskUseCase,
    private val taskByIdUseCase: GetTaskByIdUseCase,
    private val deletedTaskById: DeleteTaskByIdUseCase
) : ViewModel() {

    val allTasks = MutableLiveData<ArrayList<Task>>()
    val savedTask = MutableLiveData<Task?>()
    val taskById = MutableLiveData<Task?>()
    val deletedTask = MutableLiveData<Task?>()

    fun getAllTasks() {
        viewModelScope.launch {
            allTasks.postValue(getAllTasksUseCase())
        }
    }

    fun getTaskById(taskId: Long) {
        viewModelScope.launch {
            taskById.postValue(taskByIdUseCase(taskId))
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            savedTask.postValue(savedTaskUseCase(task))
        }
    }

    fun deleteTaskById(idTask: Long) {
        viewModelScope.launch {
            deletedTask.postValue(deletedTaskById(idTask))
        }
    }

}