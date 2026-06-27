package com.example.infratrackmobile.features.auth.data.remote.api

import com.example.infratrackmobile.features.auth.data.remote.dto.LoginRequestDto
import com.example.infratrackmobile.features.auth.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
