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
    val expectedCompletionDate: String? = null,
    val templateId: Long? = null,
    val templateName: String? = null,
    val hasChecklist: Boolean,
    val issueIdentified: Boolean
)
