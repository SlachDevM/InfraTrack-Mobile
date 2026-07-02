package com.example.infratrackmobile.features.inspection.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.inspection.presentation.component.AssignedInspectionCard
import com.example.infratrackmobile.features.inspection.presentation.viewmodel.AssignedInspectionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignedInspectionsScreen(
    onBackClick: () -> Unit,
    onInspectionClick: (Long) -> Unit,
    viewModel: AssignedInspectionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInspections()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Inspections") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = viewModel::loadInspections) {
                            Text("Retry")
                        }
                    }
                } else if (uiState.inspections.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No inspections assigned.")
                    }
                } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.inspections) { inspection ->
                        AssignedInspectionCard(
                            inspection = inspection,
                            onClick = { onInspectionClick(inspection.inspectionId) }
                        )
                    }
                }
                }
            }
        }
    }
}
