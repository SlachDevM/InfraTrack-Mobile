package com.example.infratrackmobile.features.auth.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.auth.data.repository.AuthRepository
import com.example.infratrackmobile.features.auth.domain.model.CurrentUser
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<CurrentUser> {
        return authRepository.getCurrentUser()
    }
}
