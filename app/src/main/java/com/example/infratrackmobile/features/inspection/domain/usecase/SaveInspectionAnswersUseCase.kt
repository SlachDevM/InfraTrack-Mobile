package com.example.infratrackmobile.features.inspection.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.domain.model.InspectionAnswerInput
import javax.inject.Inject

class SaveInspectionAnswersUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(inspectionId: Long, answers: List<InspectionAnswerInput>): Result<Unit> {
        return repository.saveAnswers(inspectionId, answers)
    }
}
