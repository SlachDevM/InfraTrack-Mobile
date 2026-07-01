package com.example.infratrackmobile.core.di

import com.example.infratrackmobile.features.dashboard.data.remote.api.DashboardApi
import com.example.infratrackmobile.features.dashboard.data.repository.DashboardRepository
import com.example.infratrackmobile.features.dashboard.data.repository.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository

    companion object {
        @Provides
        @Singleton
        fun provideDashboardApi(retrofit: Retrofit): DashboardApi {
            return retrofit.create(DashboardApi::class.java)
        }
    }
}
