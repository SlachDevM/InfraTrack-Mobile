package com.example.infratrackmobile.core.security

import com.example.infratrackmobile.core.security.model.LocalSession
import kotlinx.coroutines.flow.Flow

/**
 * SessionManager is the local authenticated session state holder.
 * The backend remains the source of truth for authentication validity, roles and permissions.
 */
interface SessionManager {
    suspend fun saveSession(session: LocalSession)
    fun observeSession(): Flow<LocalSession?>
    suspend fun getSession(): LocalSession?
    suspend fun clearSession()
    suspend fun getAccessToken(): String?
}
