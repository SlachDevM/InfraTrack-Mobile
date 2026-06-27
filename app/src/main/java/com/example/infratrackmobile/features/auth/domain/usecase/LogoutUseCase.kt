package com.example.infratrackmobile.features.auth.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.auth.data.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
