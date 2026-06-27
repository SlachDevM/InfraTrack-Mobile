package com.example.infratrackmobile.core.security

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip token attachment for login endpoint
        val isLoginRequest = originalRequest.url.encodedPath.contains("/api/auth/login")
        
        if (isLoginRequest) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { sessionManager.getAccessToken() }
        
        val authenticatedRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}
