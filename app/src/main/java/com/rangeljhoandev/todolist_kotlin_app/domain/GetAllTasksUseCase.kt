package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() = repository.getAllTasks()
}