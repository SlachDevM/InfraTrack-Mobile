package com.example.infratrackmobile.features.workorder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkOrderDto(
    val workOrderId: Long,
    val assetId: Long,
    val assetName: String,
    val status: String,
    val priority: String,
    val description: String,
    val assignedAt: String? = null
)
