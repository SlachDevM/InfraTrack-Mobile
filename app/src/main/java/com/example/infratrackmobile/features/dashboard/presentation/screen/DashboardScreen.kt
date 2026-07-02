package com.example.infratrackmobile.features.dashboard.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.dashboard.presentation.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAssignedInspectionsClick: () -> Unit,
    onAssignedWorkOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mobile Dashboard") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
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
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val errorMessage = uiState.errorMessage
                if (errorMessage != null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                        Button(onClick = viewModel::loadDashboard) {
                            Text("Retry")
                        }
                    }
                } else {
                    uiState.dashboard?.let { dashboard ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DashboardCounterCard(
                                label = "Assigned Inspections",
                                count = dashboard.assignedInspections,
                                onClick = onAssignedInspectionsClick
                            )
                            DashboardCounterCard(
                                label = "Assigned Work Orders",
                                count = dashboard.assignedWorkOrders,
                                onClick = onAssignedWorkOrdersClick
                            )
                            DashboardCounterCard("Overdue Inspections", dashboard.overdueInspections, isCritical = true)
                            DashboardCounterCard("Overdue Work Orders", dashboard.overdueWorkOrders, isCritical = true)
                            DashboardCounterCard("Completed Today", dashboard.completedToday)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCounterCard(
    label: String,
    count: Int,
    isCritical: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = if (isCritical && count > 0) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = if (isCritical && count > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
