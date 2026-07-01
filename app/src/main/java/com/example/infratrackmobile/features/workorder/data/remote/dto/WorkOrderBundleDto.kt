package com.example.infratrackmobile.features.workorder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkOrderBundleDto(
    val workOrder: WorkOrderDetailDto,
    val asset: WorkOrderAssetSummaryDto,
    val issue: WorkOrderIssueSummaryDto? = null,
    val decision: WorkOrderDecisionSummaryDto? = null,
    val maintenanceActivity: WorkOrderMaintenanceActivitySummaryDto? = null,
    val allowedActions: WorkOrderAllowedActionsDto
)

@Serializable
data class WorkOrderDetailDto(
    val id: Long,
    val status: String,
    val priority: String,
    val description: String,
    val assignedTo: Long? = null
)

@Serializable
data class WorkOrderAssetSummaryDto(
    val id: Long,
    val name: String,
    val category: String,
    val department: String,
    val location: String
)

@Serializable
data class WorkOrderIssueSummaryDto(
    val issueId: Long,
    val issueDescription: String,
    val issueSeverity: String
)

@Serializable
data class WorkOrderDecisionSummaryDto(
    val operationalDecisionId: Long,
    val decisionType: String
)

@Serializable
data class WorkOrderMaintenanceActivitySummaryDto(
    val maintenanceActivityId: Long,
    val status: String,
    val notes: String,
    val completedAt: String? = null
)

@Serializable
data class WorkOrderAllowedActionsDto(
    val canCompleteMaintenance: Boolean,
    val canUploadDocument: Boolean,
    val canViewAsset: Boolean
)
