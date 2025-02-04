package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()

    protected fun <T> executeUseCase(
        useCase: suspend () -> Result<T>,
        mutableLiveData: MutableLiveData<T>
    ) {
        viewModelScope.launch {
            useCase().onSuccess { result ->
                mutableLiveData.postValue(result)
            }.onFailure { exception ->
                errorMessage.postValue(exception.message ?: "Unknown error")
            }
        }
    }

}