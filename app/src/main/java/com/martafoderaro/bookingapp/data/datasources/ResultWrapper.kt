package com.martafoderaro.bookingapp.data.datasources

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: Throwable? = null): ResultWrapper<Nothing>()
    data class NetworkError(val message: String?): ResultWrapper<Nothing>()
}