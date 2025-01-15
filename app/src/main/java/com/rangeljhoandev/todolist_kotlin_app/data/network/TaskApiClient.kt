package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import retrofit2.Response
import retrofit2.http.GET

interface TaskApiClient {

    @GET("all")
    suspend fun getAllTasks(): Response<ArrayList<Task>>

}