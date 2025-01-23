package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository

class GetTaskByIdUseCase {
    private val repository = TaskRepository()

    suspend operator fun invoke(taskId: Long) = repository.getTaskById(taskId)
}