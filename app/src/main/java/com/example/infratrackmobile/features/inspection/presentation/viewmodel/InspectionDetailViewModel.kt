package com.example.infratrackmobile.features.inspection.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.navigation.Screen
import com.example.infratrackmobile.features.inspection.domain.usecase.GetInspectionBundleUseCase
import com.example.infratrackmobile.features.inspection.presentation.state.InspectionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionDetailViewModel @Inject constructor(
    private val getInspectionBundleUseCase: GetInspectionBundleUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionDetailUiState())
    val uiState: StateFlow<InspectionDetailUiState> = _uiState.asStateFlow()

    private val inspectionId: Long = try {
        // Accessing inspectionId from navigation route
        savedStateHandle.toRoute<Screen.InspectionDetail>().id.toLong()
    } catch (e: Exception) {
        -1L
    }

    init {
        loadBundle()
    }

    fun loadBundle() {
        if (inspectionId == -1L) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid Inspection ID")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = getInspectionBundleUseCase(inspectionId)) {
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
                        errorMessage = result.exception.message ?: "Failed to load inspection details"
                    )
                }
                is Result.Loading -> {
                    // Handled above
                }
            }
        }
    }
}
