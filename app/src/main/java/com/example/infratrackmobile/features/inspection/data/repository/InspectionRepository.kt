package com.example.infratrackmobile.features.inspection.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.inspection.domain.model.AssignedInspection
import com.example.infratrackmobile.features.inspection.domain.model.InspectionAnswerInput
import com.example.infratrackmobile.features.inspection.domain.model.InspectionBundle
import com.example.infratrackmobile.features.inspection.domain.model.PhysicalCondition

interface InspectionRepository {
    suspend fun getAssignedInspections(): Result<List<AssignedInspection>>
    suspend fun getInspectionBundle(inspectionId: Long): Result<InspectionBundle>
    suspend fun saveAnswers(
        inspectionId: Long,
        observedCondition: PhysicalCondition?,
        observations: String?,
        issueIdentified: Boolean?,
        answers: List<InspectionAnswerInput>
    ): Result<Unit>
    suspend fun completeInspection(
        inspectionId: Long,
        observedCondition: PhysicalCondition,
        observations: String,
        issueIdentified: Boolean,
        answers: List<InspectionAnswerInput>
    ): Result<Unit>
}
