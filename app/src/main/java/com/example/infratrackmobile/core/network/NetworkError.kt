package com.example.infratrackmobile.core.network

import retrofit2.HttpException
import java.io.IOException

sealed class NetworkError : Throwable() {
    data object NoInternet : NetworkError()
    data object Unauthorized : NetworkError()
    data object ServerError : NetworkError()
    data class Unknown(val errorMessage: String?) : NetworkError()
}

fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is IOException -> NetworkError.NoInternet
        is HttpException -> {
            when (this.code()) {
                401 -> NetworkError.Unauthorized
                500 -> NetworkError.ServerError
                else -> NetworkError.Unknown("HTTP ${this.code()}")
            }
        }
        else -> NetworkError.Unknown(this.message)
    }
}
