package com.example.infratrackmobile.features.inspection.domain.model

data class InspectionAnswerInput(
    val questionId: Long,
    val booleanValue: Boolean? = null,
    val numberValue: Double? = null,
    val textValue: String? = null,
    val choiceCodeValue: String? = null
)
