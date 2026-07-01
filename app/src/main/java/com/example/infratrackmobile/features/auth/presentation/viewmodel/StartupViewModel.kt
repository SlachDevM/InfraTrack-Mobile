package com.example.infratrackmobile.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.auth.domain.usecase.GetCurrentUserUseCase
import com.example.infratrackmobile.features.auth.domain.usecase.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartupUiState>(StartupUiState.Loading)
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        validateSession()
    }

    fun validateSession() {
        viewModelScope.launch {
            val session = observeSessionUseCase().first()
            if (session == null) {
                _uiState.value = StartupUiState.NoSession
            } else {
                when (val result = getCurrentUserUseCase()) {
                    is Result.Success -> _uiState.value = StartupUiState.Authenticated
                    is Result.Error -> {
                        // If it's 401 or 403, the repository already cleared the session
                        // and MainActivity's session observer will pick it up, 
                        // but we need to stop showing loading here.
                        _uiState.value = StartupUiState.NoSession
                    }
                    else -> _uiState.value = StartupUiState.Loading
                }
            }
        }
    }
}

sealed interface StartupUiState {
    data object Loading : StartupUiState
    data object Authenticated : StartupUiState
    data object NoSession : StartupUiState
}
