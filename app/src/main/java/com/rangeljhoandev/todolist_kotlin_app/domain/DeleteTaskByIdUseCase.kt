package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import javax.inject.Inject

class DeleteTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(idTask: Long): Result<Task?> = repository.deleteTaskById(idTask)
}