package com.example.infratrackmobile.features.auth.domain.usecase

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.auth.data.repository.AuthRepository
import com.example.infratrackmobile.features.auth.domain.model.AuthSession
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthSession> {
        return authRepository.login(email, password)
    }
}
