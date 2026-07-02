package com.example.infratrackmobile.features.workorder.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.core.ui.util.DisplayFormatter
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
    val focusManager = LocalFocusManager.current

    val navigateBack = {
        focusManager.clearFocus()
        onBackClick()
    }

    BackHandler(enabled = uiState.isDirty) {
        viewModel.showDiscardDialog()
    }

    LaunchedEffect(uiState.completeSuccess) {
        if (uiState.completeSuccess) {
            navigateBack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    if (uiState.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideDiscardDialog,
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.hideDiscardDialog()
                    navigateBack()
                }) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDiscardDialog) {
                    Text("Continue Editing")
                }
            }
        )
    }

    if (uiState.showCompletionDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideCompletionDialog,
            title = { Text("Complete Maintenance") },
            text = { Text("Are you sure you want to record this maintenance as completed? This action is final.") },
            confirmButton = {
                Button(onClick = viewModel::completeMaintenance) {
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
                    IconButton(onClick = {
                        if (uiState.isDirty) viewModel.showDiscardDialog() else navigateBack()
                    }) {
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
                        onCompleteClick = viewModel::showCompletionDialog,
                        focusManager = focusManager
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
    onCompleteClick: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
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
                canComplete = bundle.allowedActions.canCompleteMaintenance,
                focusManager = focusManager
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
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Status", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    Text(text = DisplayFormatter.toLabel(workOrder.status), style = MaterialTheme.typography.bodyMedium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Priority", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    Text(
                        text = DisplayFormatter.toLabel(workOrder.priority),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (workOrder.priority == "URGENT" || workOrder.priority == "HIGH") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            if (workOrder.assignedToId != null) {
                Text(text = "Assigned To", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Text(text = "Employee ID: ${workOrder.assignedToId}", style = MaterialTheme.typography.bodySmall)
            }
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
    val severityColor = when (issue.issueSeverity.uppercase()) {
        "CRITICAL", "HIGH" -> MaterialTheme.colorScheme.error
        "MEDIUM" -> androidx.compose.ui.graphics.Color(0xFFEF6C00)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Severity", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                Text(
                    text = DisplayFormatter.toLabel(issue.issueSeverity),
                    style = MaterialTheme.typography.bodyMedium,
                    color = severityColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = "Description", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(text = issue.issueDescription, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DecisionSummarySection(decision: WorkOrderDecisionSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Decision Type", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(text = DisplayFormatter.toLabel(decision.decisionType), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun MaintenanceActivitySection(
    activity: WorkOrderMaintenanceActivitySummary?,
    completionNotes: String,
    onNotesChange: (String) -> Unit,
    canComplete: Boolean,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (activity != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Activity Status", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                    Text(text = DisplayFormatter.toLabel(activity.status), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
                
                Text(text = "Completion Notes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Text(text = DisplayFormatter.orNotAvailable(activity.completionNotes), style = MaterialTheme.typography.bodySmall)
                
                Text(text = "Completed At", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Text(text = DisplayFormatter.formatDateTime(activity.completedAt), style = MaterialTheme.typography.bodySmall)
            } else {
                Text(text = "No maintenance activity recorded.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            }

            if (canComplete && (activity == null || activity.status != "COMPLETED")) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = completionNotes,
                    onValueChange = onNotesChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Completion Notes *") },
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
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
