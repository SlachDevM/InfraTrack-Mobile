package com.example.infratrackmobile.features.auth.data.local

import com.example.infratrackmobile.core.security.SessionManager
import com.example.infratrackmobile.core.security.model.LocalSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : SessionManager {

    override suspend fun saveSession(session: LocalSession) {
        authLocalDataSource.saveSession(session)
    }

    override fun observeSession(): Flow<LocalSession?> {
        return authLocalDataSource.getSession()
    }

    override suspend fun getSession(): LocalSession? {
        return authLocalDataSource.getSession().first()
    }

    override suspend fun clearSession() {
        authLocalDataSource.clearSession()
    }

    override suspend fun getAccessToken(): String? {
        return authLocalDataSource.getAccessToken()
    }
}
