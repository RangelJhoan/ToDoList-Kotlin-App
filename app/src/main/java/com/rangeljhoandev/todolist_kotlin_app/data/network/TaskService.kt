package com.rangeljhoandev.todolist_kotlin_app.data.network

import android.util.Log
import com.rangeljhoandev.todolist_kotlin_app.core.RetrofitHelper
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getAllTasks(): ArrayList<Task> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(TaskApiClient::class.java).getAllTasks()
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
                val response = retrofit.create(TaskApiClient::class.java).getTaskById(taskId)
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
                val response = retrofit.create(TaskApiClient::class.java).saveTask(task)
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
                val response = retrofit.create(TaskApiClient::class.java).deleteTaskById(taskId)
                response.body()
            } catch (e: Exception) {
                e.message?.let { Log.e("ERR_SERV_DEL_TASK_BY_ID", it) }
                null
            }
        }
    }

}