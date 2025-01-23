package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiClient {

    @GET("task/all")
    suspend fun getAllTasks(): Response<ArrayList<Task>>

    @GET("task/{id}")
    suspend fun getTaskById(@Path("id") taskId: Long): Response<Task?>

    @POST("task/save")
    suspend fun saveTask(@Body task: Task): Response<Task>

    @GET("task/delete/{id}")
    suspend fun deleteTaskById(@Path("id") taskId: Long): Response<Task?>

}