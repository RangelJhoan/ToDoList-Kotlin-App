package com.rangeljhoandev.todolist_kotlin_app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rangeljhoandev.todolist_kotlin_app.data.network.TaskApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .create()

    // SERVER BASE URL = "https://todolist-kotlin-ar-latest.onrender.com/"
    // LOCALHOST BASE URL = "http://{LOCAL_IP}:8080/"
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://todolist-kotlin-ar-latest.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    fun provideTaskApiClient(retrofit: Retrofit): TaskApiClient =
        retrofit.create(TaskApiClient::class.java)

}