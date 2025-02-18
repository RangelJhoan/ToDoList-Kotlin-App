package com.rangeljhoandev.todolist_kotlin_app.data.network

import android.util.Log
import com.rangeljhoandev.todolist_kotlin_app.data.model.Task
import com.rangeljhoandev.todolist_kotlin_app.data.model.enums.TaskState
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.util.Date

class ApiServiceHelperTest {

    private val apiServiceHelperTestImpl = ApiServiceHelperTestImpl()

    @Test
    fun `If response is successful and has a body, returns the response body`() = runTest {
        // Given
        val taskResponse =
            Task(1, "Task 1", "Task Description 1", Date(), Date(), TaskState.COMPLETED.id)

        val mockResponse = mockk<Response<Task>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns taskResponse
        }

        // When
        val resultWrapped = apiServiceHelperTestImpl.testSafeApiCall { mockResponse }

        // Then
        assert(resultWrapped is ResultWrapped.Success) // Validate if it's the same class
        assertEquals(taskResponse, (resultWrapped as ResultWrapped.Success).data)
    }

    @Test
    fun `If response is successful but doesn't have a body, returns Not content error`() = runTest {
        // Given
        val response = mockk<Response<Task>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns null
        }

        // When
        val resultWrapped = apiServiceHelperTestImpl.testSafeApiCall { response }

        // Then
        assertEquals(
            resultWrapped::class.java,
            ResultWrapped.Error::class.java
        ) // Validate if it's the same namespace
        assertEquals("No content", (resultWrapped as ResultWrapped.Error).errorMessage)
    }

    @Test
    fun `If response is unsuccessful, returns an error code`() = runTest {
        // Given
        val response = mockk<Response<Task>> {
            coEvery { isSuccessful } returns false
            coEvery { code() } returns 500
        }

        // When
        val resultWrapped = apiServiceHelperTestImpl.testSafeApiCall { response }

        // Then
        assert(resultWrapped is ResultWrapped.Error)
        assertEquals(
            "Error in the server response with code 500",
            (resultWrapped as ResultWrapped.Error).errorMessage
        )
    }

    @Test
    fun `If there's a connection error, returns that message`() = runTest {
        // Given
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        // When
        val resultWrapped =
            apiServiceHelperTestImpl.testSafeApiCall<Task> { throw Exception("Network error") }

        // Then
        assert(resultWrapped is ResultWrapped.Error)
        assertEquals("Connection error", (resultWrapped as ResultWrapped.Error).errorMessage)
    }

}