package com.example.infratrackmobile.features.workorder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceActivityResponseDto(
    val id: Long,
    val workOrderId: Long,
    val assetId: Long,
    val assetName: String,
    val performedByUserId: Long,
    val completionNotes: String,
    val completedAt: String,
    val workOrderStatus: String,
    val completionReviewDecision: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
