package com.abdelmageed.baimstask.data.locale

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.abdelmageed.baimstask.data.model.WeatherModel

@Entity(tableName = "weatherDB")
@TypeConverters(ListConverters::class)
data class WeatherDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String? = null,
    @TypeConverters(ListConverters::class)
    val conditions: List<WeatherModel> = emptyList(),

    )