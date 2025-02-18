package com.rangeljhoandev.todolist_kotlin_app.data.network

import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import retrofit2.Response

class ApiServiceHelperTestImpl : ApiServiceHelper() {

    suspend fun <T> testSafeApiCall(apiCall: suspend () -> Response<T>): ResultWrapped<T> =
        safeApiCall(apiCall)

}