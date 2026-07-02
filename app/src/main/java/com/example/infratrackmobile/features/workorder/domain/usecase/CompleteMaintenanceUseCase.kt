package com.example.infratrackmobile.features.workorder.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.workorder.data.repository.WorkOrderRepository
import com.example.infratrackmobile.features.workorder.domain.model.CompleteMaintenanceInput
import javax.inject.Inject

class CompleteMaintenanceUseCase @Inject constructor(
    private val repository: WorkOrderRepository
) {
    suspend operator fun invoke(workOrderId: Long, notes: String): Result<Unit> {
        val input = CompleteMaintenanceInput(
            completionNotes = notes
        )
        return repository.completeMaintenance(workOrderId, input)
    }
}
