package com.example.infratrackmobile.features.workorder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceActivityResponseDto(
    val id: Long,
    val workOrderId: Long,
    val assetId: Long,
    val assetName: String,
    val performedByUserId: Long,
    val notes: String? = null,
    val completionNotes: String? = null,
    val completedAt: String,
    val workOrderStatus: String,
    val completionReviewDecision: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
