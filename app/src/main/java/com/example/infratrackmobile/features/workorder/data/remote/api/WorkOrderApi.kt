package com.example.infratrackmobile.features.workorder.data.remote.api

import com.example.infratrackmobile.features.workorder.data.remote.dto.WorkOrderBundleDto
import com.example.infratrackmobile.features.workorder.data.remote.dto.WorkOrderDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkOrderApi {
    @GET("api/mobile/my-work-orders")
    suspend fun getMyWorkOrders(): List<WorkOrderDto>

    @GET("api/mobile/work-orders/{id}/bundle")
    suspend fun getWorkOrderBundle(@Path("id") id: Long): WorkOrderBundleDto
}
