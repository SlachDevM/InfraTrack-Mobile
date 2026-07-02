package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaveInspectionAnswersRequestDto(
    val observedCondition: String? = null,
    val observations: String? = null,
    val issueIdentified: Boolean? = null,
    val answers: List<InspectionAnswerRequestDto>
)

@Serializable
data class InspectionAnswerRequestDto(
    val questionId: Long,
    val booleanValue: Boolean? = null,
    val numberValue: Double? = null,
    val textValue: String? = null,
    val choiceCodeValue: String? = null
)
