package com.example.infratrackmobile.features.auth.data.repository

import com.example.infratrackmobile.core.common.Result
import com.example.infratrackmobile.core.network.NetworkError
import com.example.infratrackmobile.core.network.toNetworkError
import com.example.infratrackmobile.core.security.SessionManager
import com.example.infratrackmobile.core.security.model.LocalSession
import com.example.infratrackmobile.features.auth.data.remote.api.AuthApi
import com.example.infratrackmobile.features.auth.data.remote.dto.LoginRequestDto
import com.example.infratrackmobile.features.auth.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthSession> {
        return try {
            val request = LoginRequestDto(email = email, password = password)
            val response = authApi.login(request)
            
            val session = AuthSession(
                accessToken = response.accessToken,
                userId = response.userId,
                email = response.email,
                role = response.role
            )
            
            sessionManager.saveSession(session.toLocalSession())
            Result.Success(session)
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }

    override fun observeSession(): Flow<AuthSession?> {
        return sessionManager.observeSession().map { localSession ->
            localSession?.toAuthSession()
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            sessionManager.clearSession()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toNetworkError())
        }
    }
}

private fun AuthSession.toLocalSession() = LocalSession(
    accessToken = accessToken,
    userId = userId,
    email = email,
    role = role
)

private fun LocalSession.toAuthSession() = AuthSession(
    accessToken = accessToken,
    userId = userId,
    email = email,
    role = role
)
