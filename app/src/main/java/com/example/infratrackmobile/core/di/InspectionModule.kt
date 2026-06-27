package com.example.infratrackmobile.core.di

import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepository
import com.example.infratrackmobile.features.inspection.data.repository.InspectionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InspectionModule {

    @Binds
    @Singleton
    abstract fun bindInspectionRepository(
        inspectionRepositoryImpl: InspectionRepositoryImpl
    ): InspectionRepository
}
