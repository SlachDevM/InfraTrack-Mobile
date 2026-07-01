package com.example.infratrackmobile.features.inspection.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection
import com.example.infratrackmobile.features.inspection.domain.model.InspectionBundle

interface InspectionRepository {
    suspend fun getAssignedInspections(): Result<List<AssignedInspection>>
    suspend fun getInspectionBundle(inspectionId: Long): Result<InspectionBundle>
}
