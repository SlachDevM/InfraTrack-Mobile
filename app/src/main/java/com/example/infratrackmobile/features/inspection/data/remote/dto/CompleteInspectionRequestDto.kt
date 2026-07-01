package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteInspectionRequestDto(
    val observedCondition: String,
    val observations: String,
    val issueIdentified: Boolean,
    val completedAt: String,
    val answers: List<InspectionAnswerRequestDto>
)
