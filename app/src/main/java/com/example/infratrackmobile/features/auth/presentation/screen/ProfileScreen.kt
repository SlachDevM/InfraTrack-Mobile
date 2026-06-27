package com.example.infratrackmobile.features.auth.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.auth.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backend Connectivity Check") },
                actions = {
                    TextButton(onClick = viewModel::logout) {
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusText = if (uiState.isConnected) "Connected to InfraTrack backend" else "Not connected"
                    val statusColor = if (uiState.isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = statusColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.user?.let { user ->
                        Text("User: ${user.name}", style = MaterialTheme.typography.bodyLarge)
                        Text("Email: ${user.email}", style = MaterialTheme.typography.bodyLarge)
                        Text("Role: ${user.role}", style = MaterialTheme.typography.bodyLarge)
                        Text("Department: ${user.departmentName}", style = MaterialTheme.typography.bodyLarge)
                    }

                    uiState.errorMessage?.let { error ->
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(onClick = viewModel::loadProfile) {
                        Text("Retry Check")
                    }
                }
            }
        }
    }
}
