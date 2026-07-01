package com.example.infratrackmobile.features.dashboard.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.dashboard.domain.model.MobileDashboard

interface DashboardRepository {
    suspend fun getDashboard(): Result<MobileDashboard>
}
