package com.dsheal.youraimentor.utils

sealed class State<T> {
    data class Loading<T>(val isLoading: Boolean) : State<T>()
    data class Success<T>(val data: T? = null) : State<T>()
    data class Failure<T>(val error: Throwable? = null, val errorMessage: String? = null) : State<T>()
}
