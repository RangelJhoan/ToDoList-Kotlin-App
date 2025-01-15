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

}