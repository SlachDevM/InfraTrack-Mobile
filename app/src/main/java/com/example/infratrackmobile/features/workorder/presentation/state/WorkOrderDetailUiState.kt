package com.example.infratrackmobile.features.workorder.presentation.state

import com.example.infratrackmobile.features.workorder.domain.model.WorkOrderBundle

data class WorkOrderDetailUiState(
    val bundle: WorkOrderBundle? = null,
    val isLoading: Boolean = false,
    val isCompleting: Boolean = false,
    val completeSuccess: Boolean = false,
    val errorMessage: String? = null,
    val completionNotes: String = "",
    val showCompletionDialog: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isDirty: Boolean = false
)
