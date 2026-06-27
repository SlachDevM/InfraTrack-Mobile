package com.example.infratrackmobile.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val userId: Long,
    val email: String,
    val role: String
)
