package com.example.infratrackmobile.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object InspectionList : Screen

    @Serializable
    data class InspectionDetail(val id: String) : Screen

    @Serializable
    data object WorkOrderList : Screen
    
    @Serializable
    data object SyncStatus : Screen
}
