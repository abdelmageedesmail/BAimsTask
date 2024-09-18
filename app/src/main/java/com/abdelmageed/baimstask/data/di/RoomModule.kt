package com.abdelmageed.baimstask.data.di

import android.content.Context
import androidx.room.Room
import com.abdelmageed.baimstask.data.locale.WeatherDao
import com.abdelmageed.baimstask.data.locale.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideImagesDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: WeatherDatabase): WeatherDao {
        return db.weatherDao()
    }
}