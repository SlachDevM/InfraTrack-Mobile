package com.example.infratrackmobile.features.dashboard.data.remote.api

import com.example.infratrackmobile.features.dashboard.data.remote.dto.DashboardDto
import retrofit2.http.GET

interface DashboardApi {
    @GET("api/mobile/dashboard")
    suspend fun getDashboard(): DashboardDto
}
