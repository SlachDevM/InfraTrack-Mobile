package com.example.infratrackmobile.features.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("token")
    val accessToken: String,
    val userId: Long,
    val email: String,
    val role: String
)
