package com.example.infratrackmobile.features.auth.domain.model

data class CurrentUser(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val departmentId: Long?,
    val departmentName: String?,
    val enabled: Boolean
)
