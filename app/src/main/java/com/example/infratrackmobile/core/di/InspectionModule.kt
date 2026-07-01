package com.example.infratrackmobile.core.di

import com.example.infratrackmobile.features.inspection.data.remote.api.InspectionApi
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InspectionModule {

    @Binds
    @Singleton
    abstract fun bindInspectionRepository(
        inspectionRepositoryImpl: InspectionRepositoryImpl
    ): InspectionRepository

    companion object {
        @Provides
        @Singleton
        fun provideInspectionApi(retrofit: Retrofit): InspectionApi {
            return retrofit.create(InspectionApi::class.java)
        }
    }
}
