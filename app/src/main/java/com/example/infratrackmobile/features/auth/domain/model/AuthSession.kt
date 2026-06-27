package com.example.infratrackmobile.features.auth.domain.model

data class AuthSession(
    val accessToken: String,
    val userId: Long,
    val email: String,
    val role: String
)
