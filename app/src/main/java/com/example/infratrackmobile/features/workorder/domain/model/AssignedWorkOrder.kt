package com.example.infratrackmobile.features.workorder.domain.model

data class AssignedWorkOrder(
    val id: Long,
    val assetId: Long,
    val assetName: String,
    val status: String,
    val priority: String,
    val description: String,
    val assignedAt: String?
)
