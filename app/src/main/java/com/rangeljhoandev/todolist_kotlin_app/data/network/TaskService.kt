package com.rangeljhoandev.todolist_kotlin_app.data.network

import android.util.Log
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskService @Inject constructor(
    private val taskApiClient: TaskApiClient
) {

    suspend fun getAllTasks(): ArrayList<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskApiClient.getAllTasks()
                response.body() ?: arrayListOf()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERR_SERVICE_ALL_TASKS", it) }
                arrayListOf()
            }
        }
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskApiClient.getTaskById(taskId)
                response.body()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERR_SERVICE_TASK_BY_ID", it) }
                null
            }
        }
    }

    suspend fun saveTask(task: Task): Task? {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskApiClient.saveTask(task)
                response.body()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERR_SERVICE_SAVE_TASK", it) }
                null
            }
        }
    }

    suspend fun deleteTaskById(taskId: Long): Task? {
        return withContext(Dispatchers.IO) {
            try {
                val response = taskApiClient.deleteTaskById(taskId)
                response.body()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERR_SERV_DEL_TASK_BY_ID", it) }
                null
            }
        }
    }

}