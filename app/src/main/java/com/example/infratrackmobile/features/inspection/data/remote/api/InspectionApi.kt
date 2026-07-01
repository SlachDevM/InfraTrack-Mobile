package com.example.infratrackmobile.features.inspection.data.remote.api

import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionBundleDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionDto
import retrofit2.http.GET
import retrofit2.http.Path

interface InspectionApi {
    @GET("api/mobile/my-inspections")
    suspend fun getMyInspections(): List<InspectionDto>

    @GET("api/mobile/inspections/{inspectionId}/bundle")
    suspend fun getInspectionBundle(@Path("inspectionId") inspectionId: Long): InspectionBundleDto
}
