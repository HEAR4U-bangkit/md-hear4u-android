package com.bangkit.hear4u.di

sealed class StateResult<out R> private constructor() {
    data class Success<out T>(val data: T) : StateResult<T>()
    data class Error(val error:  String) : StateResult<Nothing>()
    object Loading : StateResult<Nothing>()
}