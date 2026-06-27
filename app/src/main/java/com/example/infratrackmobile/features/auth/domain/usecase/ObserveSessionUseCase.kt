package com.example.infratrackmobile.features.auth.domain.usecase

import com.example.infratrackmobile.features.auth.data.repository.AuthRepository
import com.example.infratrackmobile.features.auth.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthSession?> {
        return authRepository.observeSession()
    }
}
