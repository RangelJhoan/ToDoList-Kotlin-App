package com.rangeljhoandev.todolist_kotlin_app.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class ApiServiceHelper {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("No content"))
                } else {
                    Result.failure(Exception("Error in the server response with code ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("API_CALL_ERROR", e.message ?: "Unknown error")
                Result.failure(Exception("Connection error"))
            }
        }
    }

}