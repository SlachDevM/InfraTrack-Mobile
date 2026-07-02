package com.example.infratrackmobile.features.inspection.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.navigation.Screen
import com.example.infratrackmobile.features.inspection.domain.model.InspectionAnswerInput
import com.example.infratrackmobile.features.inspection.domain.model.PhysicalCondition
import com.example.infratrackmobile.features.inspection.domain.usecase.CompleteInspectionUseCase
import com.example.infratrackmobile.features.inspection.domain.usecase.GetInspectionBundleUseCase
import com.example.infratrackmobile.features.inspection.domain.usecase.SaveInspectionAnswersUseCase
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
    private val saveInspectionAnswersUseCase: SaveInspectionAnswersUseCase,
    private val completeInspectionUseCase: CompleteInspectionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionDetailUiState())
    val uiState: StateFlow<InspectionDetailUiState> = _uiState.asStateFlow()

    private val inspectionId: Long = try {
        savedStateHandle.toRoute<Screen.InspectionDetail>().id.toLong()
    } catch (_: Exception) {
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
                    val bundle = result.data
                    val initialAnswers = bundle.answers.associate { answer ->
                        answer.questionId to InspectionAnswerInput(
                            questionId = answer.questionId,
                            booleanValue = answer.booleanValue,
                            numberValue = answer.numberValue,
                            textValue = answer.textValue,
                            choiceCodeValue = answer.choiceCodeValue
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        bundle = bundle,
                        editableAnswers = initialAnswers,
                        observedCondition = bundle.inspection.observedCondition?.let { PhysicalCondition.valueOf(it) },
                        observations = bundle.inspection.observations ?: "",
                        issueIdentified = bundle.inspection.issueIdentified,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Failed to load inspection details"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun updateBooleanAnswer(questionId: Long, value: Boolean) {
        val currentAnswers = _uiState.value.editableAnswers.toMutableMap()
        currentAnswers[questionId] = InspectionAnswerInput(questionId = questionId, booleanValue = value)
        _uiState.value = _uiState.value.copy(editableAnswers = currentAnswers, isDirty = true)
    }

    fun updateTextAnswer(questionId: Long, value: String) {
        val currentAnswers = _uiState.value.editableAnswers.toMutableMap()
        currentAnswers[questionId] = InspectionAnswerInput(questionId = questionId, textValue = value)
        _uiState.value = _uiState.value.copy(editableAnswers = currentAnswers, isDirty = true)
    }

    fun updateNumberAnswer(questionId: Long, value: String) {
        val numberValue = value.toDoubleOrNull()
        val currentAnswers = _uiState.value.editableAnswers.toMutableMap()
        currentAnswers[questionId] = InspectionAnswerInput(questionId = questionId, numberValue = numberValue)
        _uiState.value = _uiState.value.copy(editableAnswers = currentAnswers, isDirty = true)
    }

    fun updateChoiceAnswer(questionId: Long, choiceCode: String) {
        val currentAnswers = _uiState.value.editableAnswers.toMutableMap()
        currentAnswers[questionId] = InspectionAnswerInput(questionId = questionId, choiceCodeValue = choiceCode)
        _uiState.value = _uiState.value.copy(editableAnswers = currentAnswers, isDirty = true)
    }

    fun updateObservedCondition(condition: PhysicalCondition) {
        _uiState.value = _uiState.value.copy(observedCondition = condition, isDirty = true)
    }

    fun updateObservations(observations: String) {
        _uiState.value = _uiState.value.copy(observations = observations, isDirty = true)
    }

    fun updateIssueIdentified(issueIdentified: Boolean) {
        _uiState.value = _uiState.value.copy(issueIdentified = issueIdentified, isDirty = true)
    }

    fun showCompletionDialog() {
        _uiState.value = _uiState.value.copy(showCompletionDialog = true)
    }

    fun hideCompletionDialog() {
        _uiState.value = _uiState.value.copy(showCompletionDialog = false)
    }

    fun showDiscardDialog() {
        _uiState.value = _uiState.value.copy(showDiscardDialog = true)
    }

    fun hideDiscardDialog() {
        _uiState.value = _uiState.value.copy(showDiscardDialog = false)
    }

    fun completeInspection() {
        val condition = _uiState.value.observedCondition
        val observations = _uiState.value.observations
        
        if (condition == null) {
            _uiState.value = _uiState.value.copy(
                showCompletionDialog = false,
                errorMessage = "Please select the observed condition"
            )
            return
        }

        if (observations.isBlank()) {
            _uiState.value = _uiState.value.copy(
                showCompletionDialog = false,
                errorMessage = "Observations are required"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCompleting = true, 
                errorMessage = null, 
                showCompletionDialog = false
            )
            
            val answers = _uiState.value.editableAnswers.values.toList()
            val result = completeInspectionUseCase(
                inspectionId = inspectionId,
                observedCondition = condition,
                observations = observations,
                issueIdentified = _uiState.value.issueIdentified,
                answers = answers
            )

            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isCompleting = false, 
                        completeSuccess = true,
                        isDirty = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCompleting = false,
                        errorMessage = result.exception.message ?: "Failed to complete inspection"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun saveAnswers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null, saveSuccess = false)
            val answers = _uiState.value.editableAnswers.values.toList()
            val result = saveInspectionAnswersUseCase(
                inspectionId = inspectionId,
                observedCondition = _uiState.value.observedCondition,
                observations = _uiState.value.observations,
                issueIdentified = _uiState.value.issueIdentified,
                answers = answers
            )
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false, 
                        saveSuccess = true,
                        isDirty = false
                    )
                    loadBundle() // Refresh to sync server state
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = result.exception.message ?: "Failed to save answers"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}
