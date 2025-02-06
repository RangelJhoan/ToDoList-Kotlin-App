package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import javax.inject.Inject

class TaskService @Inject constructor(
    private val taskApiClient: TaskApiClient
) : ApiServiceHelper() {

    suspend fun getAllTasks(): ResultWrapped<ArrayList<Task>> =
        safeApiCall { taskApiClient.getAllTasks() }

    suspend fun getTaskById(taskId: Long): ResultWrapped<Task?> =
        safeApiCall { taskApiClient.getTaskById(taskId) }

    suspend fun saveTask(task: Task): ResultWrapped<Task?> = safeApiCall { taskApiClient.saveTask(task) }

    suspend fun deleteTaskById(taskId: Long): ResultWrapped<Task?> =
        safeApiCall { taskApiClient.deleteTaskById(taskId) }

}
