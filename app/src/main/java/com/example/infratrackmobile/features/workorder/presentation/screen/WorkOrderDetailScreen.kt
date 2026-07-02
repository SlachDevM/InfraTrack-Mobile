package com.example.infratrackmobile.features.workorder.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.workorder.domain.model.*
import com.example.infratrackmobile.features.workorder.presentation.viewmodel.WorkOrderDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkOrderDetailScreen(
    onBackClick: () -> Unit,
    viewModel: WorkOrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.completeSuccess) {
        if (uiState.completeSuccess) {
            onBackClick()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    if (uiState.showCompletionDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideCompletionDialog,
            title = { Text("Complete Maintenance") },
            text = { Text("Are you sure you want to record this maintenance as completed? This action is final.") },
            confirmButton = {
                TextButton(onClick = viewModel::completeMaintenance) {
                    Text("Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideCompletionDialog) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Work Order Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.bundle == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null && uiState.bundle == null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::loadBundle) {
                        Text("Retry")
                    }
                }
            } else {
                uiState.bundle?.let { bundle ->
                    WorkOrderBundleContent(
                        bundle = bundle,
                        completionNotes = uiState.completionNotes,
                        isCompleting = uiState.isCompleting,
                        onNotesChange = viewModel::updateCompletionNotes,
                        onCompleteClick = viewModel::showCompletionDialog
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkOrderBundleContent(
    bundle: WorkOrderBundle,
    completionNotes: String,
    isCompleting: Boolean,
    onNotesChange: (String) -> Unit,
    onCompleteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader("Work Order")
            WorkOrderSummarySection(bundle.workOrder)
        }

        item {
            SectionHeader("Asset")
            AssetSummarySection(bundle.asset)
        }

        bundle.issue?.let { issue ->
            item {
                SectionHeader("Linked Issue")
                IssueSummarySection(issue)
            }
        }

        bundle.decision?.let { decision ->
            item {
                SectionHeader("Operational Decision")
                DecisionSummarySection(decision)
            }
        }

        item {
            SectionHeader("Maintenance Activity")
            MaintenanceActivitySection(
                activity = bundle.maintenanceActivity,
                completionNotes = completionNotes,
                onNotesChange = onNotesChange,
                canComplete = bundle.allowedActions.canCompleteMaintenance
            )
        }

        item {
            SectionHeader("Actions")
            AllowedActionsSection(
                allowedActions = bundle.allowedActions,
                isCompleting = isCompleting,
                onCompleteClick = onCompleteClick
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun WorkOrderSummarySection(workOrder: WorkOrderDetail) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = workOrder.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Status: ${workOrder.status}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Priority: ${workOrder.priority}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Assigned To ID: ${workOrder.assignedToId ?: "Unassigned"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun AssetSummarySection(asset: WorkOrderAssetSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = asset.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Category: ${asset.category}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Department: ${asset.department}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Location: ${asset.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun IssueSummarySection(issue: WorkOrderIssueSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Severity: ${issue.issueSeverity}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            Text(text = issue.issueDescription, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DecisionSummarySection(decision: WorkOrderDecisionSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Decision Type: ${decision.decisionType}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun MaintenanceActivitySection(
    activity: WorkOrderMaintenanceActivitySummary?,
    completionNotes: String,
    onNotesChange: (String) -> Unit,
    canComplete: Boolean
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (activity != null) {
                Text(text = "Status: ${activity.status}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Notes: ${activity.completionNotes ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Completed At: ${activity.completedAt ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
            } else {
                Text(text = "No maintenance activity recorded.", style = MaterialTheme.typography.bodyMedium)
            }

            if (canComplete && (activity == null || activity.status != "COMPLETED")) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = completionNotes,
                    onValueChange = onNotesChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Completion Notes *") },
                    minLines = 3
                )
            }
        }
    }
}

@Composable
private fun AllowedActionsSection(
    allowedActions: WorkOrderAllowedActions,
    isCompleting: Boolean,
    onCompleteClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (allowedActions.canCompleteMaintenance) {
            Button(
                onClick = onCompleteClick, 
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCompleting
            ) {
                if (isCompleting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Complete Maintenance")
                }
            }
        }
        if (allowedActions.canUploadDocument) {
            OutlinedButton(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Upload Document (Planned)")
            }
        }
    }
}
