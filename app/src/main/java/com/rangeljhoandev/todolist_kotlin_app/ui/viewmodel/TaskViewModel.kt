package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.domain.DeleteTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetAllTasksUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.SaveTaskUseCase
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    val allTasks = MutableLiveData<ArrayList<Task>>()
    var getAllTasksUseCase = GetAllTasksUseCase()

    val savedTask = MutableLiveData<Task?>()
    var savedTaskUseCase = SaveTaskUseCase()

    val taskById = MutableLiveData<Task?>()
    var taskByIdUseCase = GetTaskByIdUseCase()

    val deletedTask = MutableLiveData<Task?>()
    var deletedTaskById = DeleteTaskByIdUseCase()

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