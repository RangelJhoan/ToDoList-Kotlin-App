package com.rangeljhoandev.todolist_kotlin_app.data

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.network.TaskService

class TaskRepository {
    private val service = TaskService()

    suspend fun getAllTasks(): ArrayList<Task> {
        val response = service.getAllTasks()
        return response
    }

}