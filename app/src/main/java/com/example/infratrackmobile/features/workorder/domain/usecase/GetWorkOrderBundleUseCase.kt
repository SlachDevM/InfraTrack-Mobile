package com.example.infratrackmobile.features.workorder.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.workorder.data.repository.WorkOrderRepository
import com.example.infratrackmobile.features.workorder.domain.model.WorkOrderBundle
import javax.inject.Inject

class GetWorkOrderBundleUseCase @Inject constructor(
    private val repository: WorkOrderRepository
) {
    suspend operator fun invoke(id: Long): Result<WorkOrderBundle> {
        return repository.getWorkOrderBundle(id)
    }
}
