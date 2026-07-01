package com.example.infratrackmobile.features.workorder.presentation.state

import com.example.infratrackmobile.features.workorder.domain.model.AssignedWorkOrder

data class WorkOrderListUiState(
    val workOrders: List<AssignedWorkOrder> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
