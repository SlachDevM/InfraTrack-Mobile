package com.example.infratrackmobile.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUserDto(
    val userId: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)
