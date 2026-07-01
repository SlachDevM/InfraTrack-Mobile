package com.example.infratrackmobile.features.workorder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.workorder.domain.usecase.GetMyWorkOrdersUseCase
import com.example.infratrackmobile.features.workorder.presentation.state.WorkOrderListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkOrderListViewModel @Inject constructor(
    private val getMyWorkOrdersUseCase: GetMyWorkOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkOrderListUiState())
    val uiState: StateFlow<WorkOrderListUiState> = _uiState.asStateFlow()

    init {
        loadWorkOrders()
    }

    fun loadWorkOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = getMyWorkOrdersUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        workOrders = result.data,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to load work orders"
                    )
                }
                is Result.Loading -> {
                    // Handled above
                }
            }
        }
    }
}
