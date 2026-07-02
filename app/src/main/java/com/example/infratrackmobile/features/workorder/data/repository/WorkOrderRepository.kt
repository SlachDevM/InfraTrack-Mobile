package com.example.infratrackmobile.features.workorder.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.workorder.domain.model.AssignedWorkOrder
import com.example.infratrackmobile.features.workorder.domain.model.CompleteMaintenanceInput
import com.example.infratrackmobile.features.workorder.domain.model.WorkOrderBundle

interface WorkOrderRepository {
    suspend fun getMyWorkOrders(): Result<List<AssignedWorkOrder>>
    suspend fun getWorkOrderBundle(id: Long): Result<WorkOrderBundle>
    suspend fun completeMaintenance(id: Long, input: CompleteMaintenanceInput): Result<Unit>
}
