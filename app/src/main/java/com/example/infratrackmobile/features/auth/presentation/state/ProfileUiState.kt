package com.example.infratrackmobile.features.auth.presentation.state

import com.example.infratrackmobile.features.auth.domain.model.CurrentUser

data class ProfileUiState(
    val user: CurrentUser? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isConnected: Boolean = false
)
