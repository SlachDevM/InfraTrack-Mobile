package com.example.infratrackmobile.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val departmentId: Long? = null,
    val departmentName: String? = null,
    val enabled: Boolean
)
