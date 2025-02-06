package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.domain.DeleteTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetAllTasksUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.GetTaskByIdUseCase
import com.rangeljhoandev.todolist_kotlin_app.domain.SaveTaskUseCase
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @RelaxedMockK
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @RelaxedMockK
    private lateinit var saveTaskUseCase: SaveTaskUseCase

    @RelaxedMockK
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @RelaxedMockK
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase

    private lateinit var taskViewModel: TaskViewModel

    @get:Rule
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        taskViewModel = TaskViewModel(
            getAllTasksUseCase,
            saveTaskUseCase,
            getTaskByIdUseCase,
            deleteTaskByIdUseCase
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    // GetAllTaskUseCase Tests

    @Test
    fun `If the GetAllTaskUseCase response is successful, allTasks is updated`() =
        runTest {
            // Given
            val mockResource = ResultWrapped.Success(
                arrayListOf(
                    Task(1, "Task 1", "Description 1", Date(), Date(), 1),
                    Task(2, "Task 2", "Description 2", Date(), Date(), 1),
                    Task(3, "Task 3", "Description 3", Date(), Date(), 1)
                )
            )
            coEvery { getAllTasksUseCase() } returns mockResource

            // When
            taskViewModel.getAllTasks()

            // Then
            coVerify(exactly = 1) { getAllTasksUseCase() }
            assert(taskViewModel.allTasks.value == mockResource.data)

        }

    @Test
    fun `If the GetAllTasksUseCase response is an error, allTasks is not initialized`() =
        runTest {
            // Given
            val errorResource = ResultWrapped.Error<ArrayList<Task>>("Connection error")
            coEvery { getAllTasksUseCase() } returns errorResource

            // When
            taskViewModel.getAllTasks()

            // Then
            assert(taskViewModel.allTasks.value == null)
        }

    @Test
    fun `If an error occurs in the GetAllTaskUseCase execution, return an error message`() =
        runTest {
            // Given
            val resource = ResultWrapped.Error<ArrayList<Task>>("Connection error")
            coEvery { getAllTasksUseCase() } returns resource

            // When
            taskViewModel.getAllTasks()

            // Then
            assert(taskViewModel.errorMessage.value.equals(resource.errorMessage))
        }

    // GetTaskByIdUseCase Tests

    @Test
    fun `If the GetTaskById use case executes correctly and returns a task, set it in taskById livedata`() =
        runTest {
            // Given
            val task = Task(1, "Task 1", "Description 1", Date(), Date(), 1)
            coEvery { getTaskByIdUseCase(task.id!!) } returns ResultWrapped.Success(task)

            // When
            taskViewModel.getTaskById(task.id!!)

            // Then
            assertEquals(taskViewModel.taskById.value, task)
            assertNull(taskViewModel.errorMessage.value)
        }

    @Test
    fun `If the GetTaskById use case executes correctly but the task doesn't exist, taskById is not updated and errorMessage is set`() =
        runTest {
            // Given
            val taskId = 1L
            val errorMessage = "No content"
            coEvery { getTaskByIdUseCase(taskId) } returns ResultWrapped.Error(errorMessage)

            // When
            taskViewModel.getTaskById(taskId)

            // Then
            assertEquals(errorMessage, taskViewModel.errorMessage.value)
            assertNull(taskViewModel.taskById.value)
        }

    // SaveTask Tests

    @Test
    fun `If the SaveTask use case executes correctly and returns the saved task, set it in savedTask livedata`() =
        runTest {
            // Given
            val taskToSave = Task(null, "Task 1", "Description 1", Date(), Date(), 1)
            val savedTask = Task(1, "Task 1", "Description 1", Date(), Date(), 1)
            coEvery { saveTaskUseCase(taskToSave) } returns ResultWrapped.Success(savedTask)

            // When
            taskViewModel.saveTask(taskToSave)

            // Then
            assertEquals(taskViewModel.savedTask.value, savedTask)
            assertNull(taskViewModel.errorMessage.value)
        }

    @Test
    fun `If SaveTask use case executes correctly but an error occurs in the response, savedTask is not initialized and errorMessage is set`() =
        runTest {
            // Given
            val taskToSave = Task(null, "Task 1", "Description 1", Date(), Date(), 1)
            val errorMessage = "No content"
            coEvery { saveTaskUseCase(taskToSave) } returns ResultWrapped.Error(errorMessage)

            // When
            taskViewModel.saveTask(taskToSave)

            // Then
            assertEquals(errorMessage, taskViewModel.errorMessage.value)
            assertNull(taskViewModel.savedTask.value)
        }

    // DeleteTask Tests

    @Test
    fun `If the DeleteTask use case executes correctly and returns the deleted task, set it in deleteTask livedata`() =
        runTest {
            // Given
            val taskToDelete = Task(1, "Task 1", "Description 1", Date(), Date(), 1)
            coEvery { deleteTaskByIdUseCase(taskToDelete.id!!) } returns ResultWrapped.Success(
                taskToDelete
            )

            // When
            taskViewModel.deleteTaskById(taskToDelete.id!!)

            // Then
            assertEquals(taskViewModel.deletedTask.value, taskToDelete)
            assertNull(taskViewModel.errorMessage.value)
        }

    @Test
    fun `If DeleteTask use case executes correctly but an error occurs in the response, deletedTask is not initialized and errorMessage is set`() =
        runTest {
            // Given
            val taskId = 1L
            val errorMessage = "No content"
            coEvery { deleteTaskByIdUseCase(taskId) } returns ResultWrapped.Error(
                errorMessage
            )

            // When
            taskViewModel.deleteTaskById(taskId)

            // Then
            assertEquals(errorMessage, taskViewModel.errorMessage.value)
            assertNull(taskViewModel.deletedTask.value)
        }

}
