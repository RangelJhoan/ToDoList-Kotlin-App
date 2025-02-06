package com.rangeljhoandev.todolist_kotlin_app.data.network

import android.util.Log
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class ApiServiceHelper {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapped<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        ResultWrapped.Success(it)
                    } ?: ResultWrapped.Error("No content")
                } else {
                    ResultWrapped.Error("Error in the server response with code ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_CALL_ERROR", e.message ?: "Unknown error")
                ResultWrapped.Error("Connection error")
            }
        }
    }

}
