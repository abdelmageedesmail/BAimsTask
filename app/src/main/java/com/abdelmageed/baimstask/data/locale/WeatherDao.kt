package com.abdelmageed.baimstask.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherModel: WeatherDbModel)

    @Query("SELECT * FROM weatherDB WHERE cityName = :cityName")
    suspend fun getForecastForCity(cityName: String): WeatherDbModel?

    @Query("DELETE FROM weatherDB WHERE cityName = :cityName")
    suspend fun deleteForecastForCity(cityName: String)
}