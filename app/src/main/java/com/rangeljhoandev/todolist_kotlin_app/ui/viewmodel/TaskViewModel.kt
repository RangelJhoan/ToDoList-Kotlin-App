package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.domain.GetAllTasksUseCase
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    val allTasks = MutableLiveData<ArrayList<Task>>()
    var getAllTasks = GetAllTasksUseCase()

    init {
        viewModelScope.launch {
            allTasks.postValue(getAllTasks())
        }
    }

}