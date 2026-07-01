package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InspectionResponseDto(
    val id: Long,
    val status: String,
    val observedCondition: String? = null,
    val observations: String? = null,
    val issueIdentified: Boolean,
    val completedAt: String? = null
)
