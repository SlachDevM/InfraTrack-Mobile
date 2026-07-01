package com.example.infratrackmobile.features.dashboard.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.dashboard.data.repository.DashboardRepository
import com.example.infratrackmobile.features.dashboard.domain.model.MobileDashboard
import javax.inject.Inject

class GetMobileDashboardUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Result<MobileDashboard> {
        return repository.getDashboard()
    }
}
