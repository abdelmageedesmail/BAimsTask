package com.abdelmageed.baimstask.data.di

import com.abdelmageed.baimstask.data.locale.WeatherDao
import com.abdelmageed.baimstask.data.remote.repository.HomeRepositoryImpl
import com.abdelmageed.baimstask.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    init {
        System.loadLibrary("native-lib")
    }

    private external fun getApiKey(): String

    @Singleton
    @Provides
    fun provideHomeRepository(httpClient: HttpClient, dao: WeatherDao): HomeRepository =
        HomeRepositoryImpl(httpClient, getApiKey(), dao)
}