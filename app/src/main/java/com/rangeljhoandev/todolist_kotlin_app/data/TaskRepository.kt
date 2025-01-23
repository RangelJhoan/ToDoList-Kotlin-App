package com.rangeljhoandev.todolist_kotlin_app.data

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.network.TaskService

class TaskRepository {
    private val service = TaskService()

    suspend fun getAllTasks(): ArrayList<Task> {
        val response = service.getAllTasks()
        return response
    }

    suspend fun getTaskById(taskId: Long): Task? {
        val response = service.getTaskById(taskId)
        return response
    }

    suspend fun saveTask(task: Task): Task? {
        val response = service.saveTask(task)
        return response
    }

    suspend fun deleteTaskById(taskId: Long): Task? {
        val response = service.deleteTaskById(taskId)
        return response
    }

}