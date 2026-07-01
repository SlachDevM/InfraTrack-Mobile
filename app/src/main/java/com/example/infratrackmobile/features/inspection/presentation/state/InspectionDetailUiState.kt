package com.example.infratrackmobile.features.inspection.presentation.state

import com.example.infratrackmobile.features.inspection.domain.model.InspectionAnswerInput
import com.example.infratrackmobile.features.inspection.domain.model.InspectionBundle
import com.example.infratrackmobile.features.inspection.domain.model.PhysicalCondition

data class InspectionDetailUiState(
    val bundle: InspectionBundle? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isCompleting: Boolean = false,
    val saveSuccess: Boolean = false,
    val completeSuccess: Boolean = false,
    val errorMessage: String? = null,
    val editableAnswers: Map<Long, InspectionAnswerInput> = emptyMap(),
    val observedCondition: PhysicalCondition? = null,
    val observations: String = "",
    val issueIdentified: Boolean = false,
    val showCompletionDialog: Boolean = false
)
