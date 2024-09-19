package com.abdelmageed.baimstask.data.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WeatherDbModel::class], version = 1, exportSchema = false)
@TypeConverters(ListConverters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DATABASE_NAME = "weatherDB"
    }

}