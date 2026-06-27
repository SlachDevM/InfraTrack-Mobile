package com.example.infratrackmobile.core.di

import com.example.infratrackmobile.core.security.SessionManager
import com.example.infratrackmobile.features.auth.data.local.SessionManagerImpl
import com.example.infratrackmobile.features.auth.data.remote.api.AuthApi
import com.example.infratrackmobile.features.auth.data.repository.AuthRepository
import com.example.infratrackmobile.features.auth.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionManager(
        sessionManagerImpl: SessionManagerImpl
    ): SessionManager

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}
