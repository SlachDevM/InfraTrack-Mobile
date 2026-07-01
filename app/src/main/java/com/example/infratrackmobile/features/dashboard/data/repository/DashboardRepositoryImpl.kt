package com.example.infratrackmobile.features.dashboard.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.network.toNetworkError
import com.example.infratrackmobile.features.dashboard.data.remote.api.DashboardApi
import com.example.infratrackmobile.features.dashboard.data.remote.dto.DashboardDto
import com.example.infratrackmobile.features.dashboard.domain.model.MobileDashboard
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi
) : DashboardRepository {

    override suspend fun getDashboard(): Result<MobileDashboard> {
        return try {
            val dto = dashboardApi.getDashboard()
            Result.Success(dto.toDomain())
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }
}

private fun DashboardDto.toDomain() = MobileDashboard(
    assignedInspections = assignedInspections,
    assignedWorkOrders = assignedWorkOrders,
    overdueInspections = overdueInspections,
    overdueWorkOrders = overdueWorkOrders,
    completedToday = completedToday
)
