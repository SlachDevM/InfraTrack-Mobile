package com.example.infratrackmobile.features.inspection.domain.model

data class AssignedInspection(
    val inspectionId: String,
    val assetId: String,
    val assetName: String,
    val assetCode: String,
    val assetLocation: String,
    val status: String,
    val priority: String,
    val scheduledDate: String,
    val businessTriggerReason: String
)
