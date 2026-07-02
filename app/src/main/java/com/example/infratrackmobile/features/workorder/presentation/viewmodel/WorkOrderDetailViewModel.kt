package com.example.infratrackmobile.features.workorder.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.navigation.Screen
import com.example.infratrackmobile.features.workorder.domain.usecase.CompleteMaintenanceUseCase
import com.example.infratrackmobile.features.workorder.domain.usecase.GetWorkOrderBundleUseCase
import com.example.infratrackmobile.features.workorder.presentation.state.WorkOrderDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkOrderDetailViewModel @Inject constructor(
    private val getWorkOrderBundleUseCase: GetWorkOrderBundleUseCase,
    private val completeMaintenanceUseCase: CompleteMaintenanceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkOrderDetailUiState())
    val uiState: StateFlow<WorkOrderDetailUiState> = _uiState.asStateFlow()

    private val workOrderId: Long = try {
        savedStateHandle.toRoute<Screen.WorkOrderDetail>().id.toLong()
    } catch (_: Exception) {
        -1L
    }

    init {
        loadBundle()
    }

    fun loadBundle() {
        if (workOrderId == -1L) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid Work Order ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = getWorkOrderBundleUseCase(workOrderId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        bundle = result.data,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to load work order details"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun updateCompletionNotes(notes: String) {
        _uiState.value = _uiState.value.copy(completionNotes = notes)
    }

    fun showCompletionDialog() {
        _uiState.value = _uiState.value.copy(showCompletionDialog = true)
    }

    fun hideCompletionDialog() {
        _uiState.value = _uiState.value.copy(showCompletionDialog = false)
    }

    fun completeMaintenance() {
        if (_uiState.value.completionNotes.isBlank()) {
            _uiState.value = _uiState.value.copy(
                showCompletionDialog = false,
                errorMessage = "Completion notes are required"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCompleting = true, 
                errorMessage = null, 
                showCompletionDialog = false
            )
            
            val result = completeMaintenanceUseCase(
                workOrderId = workOrderId,
                notes = _uiState.value.completionNotes
            )

            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isCompleting = false, completeSuccess = true)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCompleting = false,
                        errorMessage = result.exception.message ?: "Failed to complete maintenance"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }
}
