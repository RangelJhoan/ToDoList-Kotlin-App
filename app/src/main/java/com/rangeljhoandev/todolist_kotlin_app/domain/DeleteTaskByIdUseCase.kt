package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository

class DeleteTaskByIdUseCase {

    private val repository = TaskRepository()

    suspend operator fun invoke(idTask: Long) = repository.deleteTaskById(idTask)

}