package com.rangeljhoandev.todolist_kotlin_app.domain

import com.rangeljhoandev.todolist_kotlin_app.data.TaskRepository
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task

class SaveTaskUseCase() {
    private val repository = TaskRepository()

    suspend operator fun invoke(task: Task) = repository.saveTask(task)

}