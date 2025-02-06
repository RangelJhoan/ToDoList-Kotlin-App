package com.rangeljhoandev.todolist_kotlin_app.data

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.network.TaskService
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val service: TaskService
) {

    suspend fun getAllTasks(): ResultWrapped<ArrayList<Task>> {
        val result = service.getAllTasks()
        return result
    }

    suspend fun getTaskById(taskId: Long): ResultWrapped<Task?> {
        val result = service.getTaskById(taskId)
        return result
    }

    suspend fun saveTask(task: Task): ResultWrapped<Task?> {
        val result = service.saveTask(task)
        return result
    }

    suspend fun deleteTaskById(taskId: Long): ResultWrapped<Task?> {
        val result = service.deleteTaskById(taskId)
        return result
    }

}
