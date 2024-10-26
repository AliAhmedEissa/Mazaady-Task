package com.mazaady.android_task.util

import com.mazaady.android_task.domain.model.CustomError

sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val error: CustomError) : DataState<Nothing>()
    data object ServerError : DataState<Nothing>()
    data object Processing: DataState<Nothing>()
    data object Idle: DataState<Nothing>()
}