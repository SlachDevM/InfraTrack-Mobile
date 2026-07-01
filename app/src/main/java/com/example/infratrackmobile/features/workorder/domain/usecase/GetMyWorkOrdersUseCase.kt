package com.example.infratrackmobile.features.workorder.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.workorder.data.repository.WorkOrderRepository
import com.example.infratrackmobile.features.workorder.domain.model.AssignedWorkOrder
import javax.inject.Inject

class GetMyWorkOrdersUseCase @Inject constructor(
    private val repository: WorkOrderRepository
) {
    suspend operator fun invoke(): Result<List<AssignedWorkOrder>> {
        return repository.getMyWorkOrders()
    }
}
