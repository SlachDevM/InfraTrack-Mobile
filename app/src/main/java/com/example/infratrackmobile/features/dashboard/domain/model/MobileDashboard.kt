package com.example.infratrackmobile.features.dashboard.domain.model

data class MobileDashboard(
    val assignedInspections: Int,
    val assignedWorkOrders: Int,
    val overdueInspections: Int,
    val overdueWorkOrders: Int,
    val completedToday: Int
)
