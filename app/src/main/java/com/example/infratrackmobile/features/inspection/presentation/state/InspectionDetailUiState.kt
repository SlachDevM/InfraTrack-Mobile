package com.example.infratrackmobile.features.inspection.presentation.state

import com.example.infratrackmobile.features.inspection.domain.model.InspectionBundle

data class InspectionDetailUiState(
    val bundle: InspectionBundle? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
