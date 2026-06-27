package com.example.infratrackmobile.features.inspection.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection
import javax.inject.Inject

class GetAssignedInspectionsUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(): Result<List<AssignedInspection>> {
        return repository.getAssignedInspections()
    }
}
