package com.rangeljhoandev.todolist_kotlin_app.data.model.enums

enum class TaskState(val id: Short, val label: String, val color: String) {
    PENDING(1, "Pending", "#FF0000"),
    COMPLETED(2, "Completed", "#00FF00")
}