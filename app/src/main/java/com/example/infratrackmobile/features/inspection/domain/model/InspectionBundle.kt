package com.example.infratrackmobile.features.inspection.domain.model

data class InspectionBundle(
    val inspection: InspectionSummary,
    val asset: AssetSummary,
    val template: TemplateSummary?,
    val questions: List<InspectionQuestion>,
    val answers: List<InspectionAnswer>,
    val allowedActions: InspectionAllowedActions
)

data class InspectionSummary(
    val id: Long,
    val status: String,
    val priority: String,
    val expectedCompletionDate: String,
    val observedCondition: String?,
    val observations: String?,
    val issueIdentified: Boolean
)

data class AssetSummary(
    val id: Long,
    val name: String,
    val category: String,
    val department: String,
    val location: String
)

data class TemplateSummary(
    val id: Long,
    val name: String,
    val version: String,
    val status: String
)

data class InspectionQuestion(
    val id: Long,
    val code: String,
    val questionText: String,
    val helpText: String?,
    val type: String,
    val required: Boolean,
    val displayOrder: Int,
    val unitSymbol: String?,
    val minValue: Double?,
    val maxValue: Double?,
    val decimalPlaces: Int?,
    val choices: List<InspectionChoice>
)

data class InspectionChoice(
    val code: String,
    val label: String,
    val displayOrder: Int
)

data class InspectionAnswer(
    val questionId: Long,
    val booleanValue: Boolean?,
    val numberValue: Double?,
    val textValue: String?,
    val choiceCodeValue: String?
)

data class InspectionAllowedActions(
    val canComplete: Boolean,
    val canUploadDocument: Boolean,
    val canViewAsset: Boolean
)
