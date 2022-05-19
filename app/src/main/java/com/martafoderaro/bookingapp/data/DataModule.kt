package com.martafoderaro.bookingapp.data

import android.content.Context
import android.content.res.AssetManager
import com.martafoderaro.bookingapp.data.datasources.FileDataSource
import com.martafoderaro.bookingapp.data.repository.BookingsRepositoryImpl
import com.martafoderaro.bookingapp.domain.datasource.DataSource
import com.martafoderaro.bookingapp.domain.repository.BookingRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideAssetManager(context: Context): AssetManager = context.assets

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindingModule {
        @Binds
        abstract fun bindBookingsRepositoryImpl(impl: BookingsRepositoryImpl): BookingRepository
        @Binds
        abstract fun bindFileDataSource(impl: FileDataSource): DataSource
    }
}