package com.example.infratrackmobile.features.inspection.data.remote.api

import com.example.infratrackmobile.features.inspection.data.remote.dto.CompleteInspectionRequestDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionAnswerResponseDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionBundleDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.InspectionResponseDto
import com.example.infratrackmobile.features.inspection.data.remote.dto.SaveInspectionAnswersRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InspectionApi {
    @GET("api/mobile/my-inspections")
    suspend fun getMyInspections(): List<InspectionDto>

    @GET("api/mobile/inspections/{inspectionId}/bundle")
    suspend fun getInspectionBundle(@Path("inspectionId") inspectionId: Long): InspectionBundleDto

    @PUT("api/inspections/{id}/answers")
    suspend fun saveAnswers(
        @Path("id") id: Long,
        @Body request: SaveInspectionAnswersRequestDto
    ): List<InspectionAnswerResponseDto>

    @POST("api/inspections/{id}/complete")
    suspend fun completeInspection(
        @Path("id") id: Long,
        @Body request: CompleteInspectionRequestDto
    ): InspectionResponseDto
}
