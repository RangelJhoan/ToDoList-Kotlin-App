package com.rangeljhoandev.todolist_kotlin_app.util

sealed class ResultWrapped<T> {
    data class Success<T>(val data: T) : ResultWrapped<T>()
    data class Error<T>(val errorMessage: String) : ResultWrapped<T>()
}
