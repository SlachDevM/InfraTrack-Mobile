package com.example.infratrackmobile.features.inspection.presentation.state

import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection

data class AssignedInspectionsUiState(
    val inspections: List<AssignedInspection> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
