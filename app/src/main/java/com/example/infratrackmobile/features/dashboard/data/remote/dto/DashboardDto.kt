package com.example.infratrackmobile.features.dashboard.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    val assignedInspections: Int,
    val assignedWorkOrders: Int,
    val overdueInspections: Int,
    val overdueWorkOrders: Int,
    val completedToday: Int
)
