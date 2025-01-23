package com.rangeljhoandev.todolist_kotlin_app.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .create()


    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.10:8080/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}