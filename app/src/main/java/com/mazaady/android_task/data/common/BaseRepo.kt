package com.mazaady.android_task.data.common

import com.mazaady.android_task.domain.model.CustomError
import com.mazaady.android_task.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

abstract class BaseRepo {
    inline fun <T> performApiCall(
        crossinline apiCall: suspend () -> Response<T?>,
    ): Flow<DataState<T?>> = flow {
        try {
            emit(DataState.Processing)
            val response = apiCall()
            if (response.isSuccessful) {
                emit(DataState.Idle)
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.Idle)
                when (response.code()) {
                    500 -> {
                        emit(DataState.ServerError)
                    }

                    else -> {
                        val errorBody = response.errorBody()?.string().toString()
                        emit(DataState.Error(CustomError(errorBody, response.code())))
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
            emit(DataState.Idle)
            emit(DataState.ServerError)
        }
    }


}