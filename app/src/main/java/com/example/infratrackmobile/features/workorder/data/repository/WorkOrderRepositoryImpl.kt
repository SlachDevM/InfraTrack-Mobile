package com.example.infratrackmobile.features.workorder.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.network.toNetworkError
import com.example.infratrackmobile.features.workorder.data.remote.api.WorkOrderApi
import com.example.infratrackmobile.features.workorder.data.remote.dto.*
import com.example.infratrackmobile.features.workorder.domain.model.*
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WorkOrderRepositoryImpl @Inject constructor(
    private val workOrderApi: WorkOrderApi
) : WorkOrderRepository {

    override suspend fun getMyWorkOrders(): Result<List<AssignedWorkOrder>> {
        return try {
            val dtos = workOrderApi.getMyWorkOrders()
            Result.Success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun getWorkOrderBundle(id: Long): Result<WorkOrderBundle> {
        return try {
            val dto = workOrderApi.getWorkOrderBundle(id)
            Result.Success(dto.toDomain())
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun completeMaintenance(id: Long, input: CompleteMaintenanceInput): Result<Unit> {
        return try {
            val request = CompleteMaintenanceRequestDto(
                completionNotes = input.completionNotes,
                completedAt = input.completedAt.format(DateTimeFormatter.ISO_DATE_TIME)
            )
            workOrderApi.completeMaintenance(id, request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }
}

private fun WorkOrderDto.toDomain() = AssignedWorkOrder(
    id = workOrderId,
    assetId = assetId,
    assetName = assetName,
    status = status,
    priority = priority,
    description = description,
    assignedAt = assignedAt
)

private fun WorkOrderBundleDto.toDomain() = WorkOrderBundle(
    workOrder = workOrder.toDomain(),
    asset = asset.toDomain(),
    issue = issue?.toDomain(),
    decision = decision?.toDomain(),
    maintenanceActivity = maintenanceActivity?.toDomain(),
    allowedActions = allowedActions.toDomain()
)

private fun WorkOrderDetailDto.toDomain() = WorkOrderDetail(
    id = id,
    status = status,
    priority = priority,
    description = description,
    assignedTo = assignedTo
)

private fun WorkOrderAssetSummaryDto.toDomain() = WorkOrderAssetSummary(
    id = id,
    name = name,
    category = category,
    department = department,
    location = location
)

private fun WorkOrderIssueSummaryDto.toDomain() = WorkOrderIssueSummary(
    issueId = issueId,
    issueDescription = issueDescription,
    issueSeverity = issueSeverity
)

private fun WorkOrderDecisionSummaryDto.toDomain() = WorkOrderDecisionSummary(
    operationalDecisionId = operationalDecisionId,
    decisionType = decisionType
)

private fun WorkOrderMaintenanceActivitySummaryDto.toDomain() = WorkOrderMaintenanceActivitySummary(
    maintenanceActivityId = maintenanceActivityId,
    status = status,
    notes = notes,
    completedAt = completedAt
)

private fun WorkOrderAllowedActionsDto.toDomain() = WorkOrderAllowedActions(
    canCompleteMaintenance = canCompleteMaintenance,
    canUploadDocument = canUploadDocument,
    canViewAsset = canViewAsset
)
