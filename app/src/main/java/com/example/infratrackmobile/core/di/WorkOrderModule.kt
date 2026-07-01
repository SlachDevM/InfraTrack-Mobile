package com.example.infratrackmobile.core.di

import com.example.infratrackmobile.features.workorder.data.remote.api.WorkOrderApi
import com.example.infratrackmobile.features.workorder.data.repository.WorkOrderRepository
import com.example.infratrackmobile.features.workorder.data.repository.WorkOrderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkOrderModule {

    @Binds
    @Singleton
    abstract fun bindWorkOrderRepository(
        workOrderRepositoryImpl: WorkOrderRepositoryImpl
    ): WorkOrderRepository

    companion object {
        @Provides
        @Singleton
        fun provideWorkOrderApi(retrofit: Retrofit): WorkOrderApi {
            return retrofit.create(WorkOrderApi::class.java)
        }
    }
}
