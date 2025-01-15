package com.rangeljhoandev.todolist_kotlin_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String,
    @SerializedName("creation_date")
    val creationDate: Date,
    @SerializedName("due_date")
    val dueDate: Date,
    val state: Short
)
