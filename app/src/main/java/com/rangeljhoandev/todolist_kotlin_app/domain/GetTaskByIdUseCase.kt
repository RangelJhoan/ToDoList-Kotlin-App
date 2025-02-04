package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Result<Task?> = repository.getTaskById(taskId)
}