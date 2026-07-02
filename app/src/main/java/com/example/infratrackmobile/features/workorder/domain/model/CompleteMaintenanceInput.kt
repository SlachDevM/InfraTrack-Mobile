package com.example.infratrackmobile.features.workorder.domain.model

import java.time.LocalDateTime

data class CompleteMaintenanceInput(
    val completionNotes: String,
    val completedAt: LocalDateTime
)
