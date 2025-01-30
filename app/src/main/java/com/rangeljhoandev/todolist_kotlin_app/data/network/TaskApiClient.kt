package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiClient {

    @GET("task/")
    suspend fun getAllTasks(): Response<ArrayList<Task>>

    @GET("task/{id}")
    suspend fun getTaskById(@Path("id") taskId: Long): Response<Task?>

    @POST("task/save")
    suspend fun saveTask(@Body task: Task): Response<Task>

    @DELETE("task/{id}")
    suspend fun deleteTaskById(@Path("id") taskId: Long): Response<Task?>

}