package com.example.infratrackmobile.features.inspection.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InspectionDto(
    val inspectionId: Long,
    val assetId: Long,
    val assetName: String,
    val assetCategoryName: String,
    val status: String,
    val priority: String,
    val expectedCompletionDate: String,
    val templateId: Long,
    val templateName: String,
    val hasChecklist: Boolean,
    val issueIdentified: Boolean
)
