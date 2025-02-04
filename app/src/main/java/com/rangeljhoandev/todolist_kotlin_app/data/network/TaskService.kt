package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import javax.inject.Inject

class TaskService @Inject constructor(
    private val taskApiClient: TaskApiClient
) : ApiServiceHelper() {

    suspend fun getAllTasks(): Result<ArrayList<Task>> = safeApiCall { taskApiClient.getAllTasks() }

    suspend fun getTaskById(taskId: Long): Result<Task?> =
        safeApiCall { taskApiClient.getTaskById(taskId) }

    suspend fun saveTask(task: Task): Result<Task?> = safeApiCall { taskApiClient.saveTask(task) }

    suspend fun deleteTaskById(taskId: Long): Result<Task?> =
        safeApiCall { taskApiClient.deleteTaskById(taskId) }

}