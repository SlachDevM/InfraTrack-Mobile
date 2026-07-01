package com.example.infratrackmobile.features.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infratrackmobile.features.auth.presentation.viewmodel.StartupUiState
import com.example.infratrackmobile.features.auth.presentation.viewmodel.StartupViewModel

@Composable
fun StartupScreen(
    onAuthenticated: () -> Unit,
    onNoSession: () -> Unit,
    viewModel: StartupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            StartupUiState.Authenticated -> onAuthenticated()
            StartupUiState.NoSession -> onNoSession()
            StartupUiState.Loading -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (uiState == StartupUiState.Loading) {
            CircularProgressIndicator()
        }
    }
}
