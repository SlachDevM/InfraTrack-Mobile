package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswerResponseDto(
    val id: Long,
    val questionId: Long,
    val booleanValue: Boolean? = null,
    val numberValue: Double? = null,
    val textValue: String? = null,
    val choiceCodeValue: String? = null
)
