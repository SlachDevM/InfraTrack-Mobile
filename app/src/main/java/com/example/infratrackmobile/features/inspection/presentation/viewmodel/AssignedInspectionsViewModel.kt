package com.example.infratrackmobile.features.inspection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.domain.usecase.GetAssignedInspectionsUseCase
import com.example.infratrackmobile.features.inspection.presentation.state.AssignedInspectionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignedInspectionsViewModel @Inject constructor(
    private val getAssignedInspectionsUseCase: GetAssignedInspectionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssignedInspectionsUiState())
    val uiState: StateFlow<AssignedInspectionsUiState> = _uiState.asStateFlow()

    init {
        loadInspections()
    }

    fun loadInspections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = getAssignedInspectionsUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        inspections = result.data,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to load inspections"
                    )
                }
                is Result.Loading -> {
                    // Handled by init copy
                }
            }
        }
    }
}
