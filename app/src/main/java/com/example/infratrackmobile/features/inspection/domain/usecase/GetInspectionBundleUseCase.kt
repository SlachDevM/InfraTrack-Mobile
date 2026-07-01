package com.example.infratrackmobile.features.inspection.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.domain.model.InspectionBundle
import javax.inject.Inject

class GetInspectionBundleUseCase @Inject constructor(
    private val repository: InspectionRepository
) {
    suspend operator fun invoke(inspectionId: Long): Result<InspectionBundle> {
        return repository.getInspectionBundle(inspectionId)
    }
}
