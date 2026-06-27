package com.example.infratrackmobile.features.auth.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.features.auth.domain.model.AuthSession
import com.example.infratrackmobile.features.auth.domain.model.CurrentUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthSession>
    fun observeSession(): Flow<AuthSession?>
    suspend fun getCurrentUser(): Result<CurrentUser>
    suspend fun logout(): Result<Unit>
}
