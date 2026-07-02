package com.example.infratrackmobile.features.workorder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteMaintenanceRequestDto(
    val completionNotes: String,
    val completedAt: String
)
