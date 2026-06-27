package com.example.infratrackmobile.core.network

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
        // More specific mapping can be added here once Retrofit response handling is implemented
        else -> NetworkError.Unknown(this.message)
    }
}
