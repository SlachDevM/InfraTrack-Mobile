package com.example.infratrackmobile.features.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.infratrackmobile.core.security.model.LocalSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val ROLE = stringPreferencesKey("role")
    }

    suspend fun saveSession(session: LocalSession) {
        dataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = session.accessToken
            preferences[Keys.USER_ID] = session.userId.toString()
            preferences[Keys.EMAIL] = session.email
            preferences[Keys.ROLE] = session.role
        }
    }

    fun getSession(): Flow<LocalSession?> {
        return dataStore.data.map { preferences ->
            val accessToken = preferences[Keys.ACCESS_TOKEN]
            val userId = preferences[Keys.USER_ID]
            val email = preferences[Keys.EMAIL]
            val role = preferences[Keys.ROLE]

            if (accessToken != null && userId != null && email != null && role != null) {
                LocalSession(
                    accessToken = accessToken,
                    userId = userId.toLong(),
                    email = email,
                    role = role
                )
            } else {
                null
            }
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[Keys.ACCESS_TOKEN]
        }.first()
    }
}
