package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InspectionBundleDto(
    val inspection: InspectionSummaryDto,
    val asset: AssetSummaryDto,
    val template: TemplateSummaryDto? = null,
    val questions: List<InspectionQuestionDto> = emptyList(),
    val answers: List<InspectionAnswerDto> = emptyList(),
    val allowedActions: InspectionAllowedActionsDto
)

@Serializable
data class InspectionSummaryDto(
    val id: Long,
    val status: String,
    val priority: String,
    val expectedCompletionDate: String,
    val observedCondition: String? = null,
    val observations: String? = null,
    val issueIdentified: Boolean
)

@Serializable
data class AssetSummaryDto(
    val id: Long,
    val name: String,
    val category: String,
    val department: String,
    val location: String
)

@Serializable
data class TemplateSummaryDto(
    val id: Long,
    val name: String,
    val version: String,
    val status: String
)

@Serializable
data class InspectionQuestionDto(
    val id: Long,
    val code: String,
    val questionText: String,
    val helpText: String? = null,
    val type: String,
    val required: Boolean,
    val displayOrder: Int,
    val unitSymbol: String? = null,
    val minValue: Double? = null,
    val maxValue: Double? = null,
    val decimalPlaces: Int? = null,
    val choices: List<InspectionChoiceDto> = emptyList()
)

@Serializable
data class InspectionChoiceDto(
    val code: String,
    val label: String,
    val displayOrder: Int
)

@Serializable
data class InspectionAnswerDto(
    val questionId: Long,
    val booleanValue: Boolean? = null,
    val numberValue: Double? = null,
    val textValue: String? = null,
    val choiceCodeValue: String? = null
)

@Serializable
data class InspectionAllowedActionsDto(
    val canComplete: Boolean,
    val canUploadDocument: Boolean,
    val canViewAsset: Boolean
)
