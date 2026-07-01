package com.example.infratrackmobile.features.inspection.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.domain.model.InspectionAnswerInput
import com.example.infratrackmobile.features.inspection.domain.model.PhysicalCondition
import java.time.LocalDateTime
import javax.inject.Inject

class CompleteInspectionUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(
        inspectionId: Long,
        observedCondition: PhysicalCondition,
        observations: String,
        issueIdentified: Boolean,
        answers: List<InspectionAnswerInput>
    ): Result<Unit> {
        return repository.completeInspection(
            inspectionId = inspectionId,
            observedCondition = observedCondition,
            observations = observations,
            issueIdentified = issueIdentified,
            completedAt = LocalDateTime.now(),
            answers = answers
        )
    }
}
