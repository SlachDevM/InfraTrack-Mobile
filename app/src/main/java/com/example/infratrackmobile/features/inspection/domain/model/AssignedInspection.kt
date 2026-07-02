package com.example.infratrackmobile.features.inspection.domain.model

data class AssignedInspection(
    val inspectionId: Long,
    val assetId: Long,
    val assetName: String,
    val assetCategoryName: String,
    val status: String,
    val priority: String,
    val expectedCompletionDate: String? = null,
    val templateName: String? = null,
    val hasChecklist: Boolean,
    val issueIdentified: Boolean
)
