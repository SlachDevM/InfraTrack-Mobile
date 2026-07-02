package com.example.infratrackmobile.features.inspection.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.network.toNetworkError
import com.example.infratrackmobile.features.inspection.data.remote.api.InspectionApi
import com.example.infratrackmobile.features.inspection.data.remote.dto.*
import com.example.infratrackmobile.features.inspection.domain.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class InspectionRepositoryImpl @Inject constructor(
    private val inspectionApi: InspectionApi
) : InspectionRepository {

    override suspend fun getAssignedInspections(): Result<List<AssignedInspection>> {
        return try {
            val response = inspectionApi.getMyInspections()
            Result.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun getInspectionBundle(inspectionId: Long): Result<InspectionBundle> {
        return try {
            val response = inspectionApi.getInspectionBundle(inspectionId)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun saveAnswers(
        inspectionId: Long,
        observedCondition: PhysicalCondition?,
        observations: String?,
        issueIdentified: Boolean?,
        answers: List<InspectionAnswerInput>
    ): Result<Unit> {
        return try {
            val request = SaveInspectionAnswersRequestDto(
                observedCondition = observedCondition?.name,
                observations = observations,
                issueIdentified = issueIdentified,
                answers = answers.map { it.toDto() }
            )
            inspectionApi.saveProgress(inspectionId, request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun completeInspection(
        inspectionId: Long,
        observedCondition: PhysicalCondition,
        observations: String,
        issueIdentified: Boolean,
        completedAt: LocalDateTime,
        answers: List<InspectionAnswerInput>
    ): Result<Unit> {
        return try {
            val request = CompleteInspectionRequestDto(
                observedCondition = observedCondition.name,
                observations = observations,
                issueIdentified = issueIdentified,
                completedAt = completedAt.format(DateTimeFormatter.ISO_DATE_TIME),
                answers = answers.map { it.toDto() }
            )
            inspectionApi.completeInspection(inspectionId, request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }
}

private fun InspectionAnswerInput.toDto() = InspectionAnswerRequestDto(
    questionId = questionId,
    booleanValue = booleanValue,
    numberValue = numberValue,
    textValue = textValue,
    choiceCodeValue = choiceCodeValue
)

private fun InspectionDto.toDomain() = AssignedInspection(
    inspectionId = inspectionId,
    assetId = assetId,
    assetName = assetName,
    assetCategoryName = assetCategoryName,
    status = status,
    priority = priority,
    expectedCompletionDate = expectedCompletionDate,
    templateName = templateName,
    hasChecklist = hasChecklist,
    issueIdentified = issueIdentified
)

private fun InspectionBundleDto.toDomain() = InspectionBundle(
    inspection = inspection.toDomain(),
    asset = asset.toDomain(),
    template = template?.toDomain(),
    questions = questions.map { it.toDomain() },
    answers = answers.map { it.toDomain() },
    allowedActions = allowedActions.toDomain()
)

private fun InspectionSummaryDto.toDomain() = InspectionSummary(
    id = id,
    status = status,
    priority = priority,
    expectedCompletionDate = expectedCompletionDate,
    observedCondition = observedCondition,
    observations = observations,
    issueIdentified = issueIdentified
)

private fun AssetSummaryDto.toDomain() = AssetSummary(
    id = id,
    name = name,
    category = category,
    department = department,
    location = location
)

private fun TemplateSummaryDto.toDomain() = TemplateSummary(
    id = id,
    name = name,
    version = version,
    status = status
)

private fun InspectionQuestionDto.toDomain() = InspectionQuestion(
    id = id,
    code = code,
    questionText = questionText,
    helpText = helpText,
    type = type,
    required = required,
    displayOrder = displayOrder,
    unitSymbol = unitSymbol,
    minValue = minValue,
    maxValue = maxValue,
    decimalPlaces = decimalPlaces,
    choices = choices.map { it.toDomain() }
)

private fun InspectionChoiceDto.toDomain() = InspectionChoice(
    code = code,
    label = label,
    displayOrder = displayOrder
)

private fun InspectionAnswerDto.toDomain() = InspectionAnswer(
    questionId = questionId,
    booleanValue = booleanValue,
    numberValue = numberValue,
    textValue = textValue,
    choiceCodeValue = choiceCodeValue
)

private fun InspectionAllowedActionsDto.toDomain() = InspectionAllowedActions(
    canComplete = canComplete,
    canUploadDocument = canUploadDocument,
    canViewAsset = canViewAsset
)
