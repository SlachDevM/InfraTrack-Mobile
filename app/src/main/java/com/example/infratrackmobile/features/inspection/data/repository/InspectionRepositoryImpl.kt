package com.example.infratrackmobile.features.inspection.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection
import javax.inject.Inject

class InspectionRepositoryImpl @Inject constructor() : InspectionRepository {
    
    override suspend fun getAssignedInspections(): Result<List<AssignedInspection>> {
        // "no api for now" as per user instruction.
        // Returning an empty list for this read-only model foundation.
        return Result.Success(emptyList())
    }
}
