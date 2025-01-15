package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository

class GetAllTasksUseCase {
    private val repository = TaskRepository()

    suspend operator fun invoke() = repository.getAllTasks()
}