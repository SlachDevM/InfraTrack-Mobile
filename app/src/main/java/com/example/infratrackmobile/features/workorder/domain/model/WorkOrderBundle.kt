package com.example.infratrackmobile.features.workorder.domain.model

data class WorkOrderBundle(
    val workOrder: WorkOrderDetail,
    val asset: WorkOrderAssetSummary,
    val issue: WorkOrderIssueSummary?,
    val decision: WorkOrderDecisionSummary?,
    val maintenanceActivity: WorkOrderMaintenanceActivitySummary?,
    val allowedActions: WorkOrderAllowedActions
)

data class WorkOrderDetail(
    val id: Long,
    val status: String,
    val priority: String,
    val description: String,
    val assignedToId: Long?
)

data class WorkOrderAssetSummary(
    val id: Long,
    val name: String,
    val category: String,
    val department: String,
    val location: String
)

data class WorkOrderIssueSummary(
    val issueId: Long,
    val issueDescription: String,
    val issueSeverity: String
)

data class WorkOrderDecisionSummary(
    val operationalDecisionId: Long,
    val decisionType: String
)

data class WorkOrderMaintenanceActivitySummary(
    val maintenanceActivityId: Long,
    val status: String,
    val completionNotes: String?,
    val completedAt: String?
)

data class WorkOrderAllowedActions(
    val canCompleteMaintenance: Boolean,
    val canUploadDocument: Boolean,
    val canViewAsset: Boolean
)
