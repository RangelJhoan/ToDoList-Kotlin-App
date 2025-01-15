package com.rangeljhoandev.todolist_kotlin_app.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.10:8080/task/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}