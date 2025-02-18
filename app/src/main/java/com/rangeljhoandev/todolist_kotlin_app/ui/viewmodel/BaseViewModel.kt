package com.rangeljhoandev.todolist_kotlin_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rangeljhoandev.todolist_kotlin_app.util.ResultWrapped
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()

    protected fun <T> executeUseCase(
        useCase: suspend () -> ResultWrapped<T>,
        mutableLiveData: MutableLiveData<T>
    ) {
        viewModelScope.launch {
            when (val resultWrapped = useCase()) {
                is ResultWrapped.Success -> mutableLiveData.postValue(resultWrapped.data)
                is ResultWrapped.Error -> errorMessage.postValue(resultWrapped.errorMessage)
            }
        }
    }

}
