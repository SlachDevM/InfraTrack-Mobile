package com.example.infratrackmobile.features.dashboard.presentation.state

import com.example.infratrackmobile.features.dashboard.domain.model.MobileDashboard

data class DashboardUiState(
    val dashboard: MobileDashboard? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
