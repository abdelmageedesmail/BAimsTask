package com.abdelmageed.baimstask.data.locale

import androidx.room.TypeConverter
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.domain.weather.WeatherType
import com.abdelmageed.baimstask.domain.weather.WeatherTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ListConverters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(WeatherType::class.java, WeatherTypeAdapter())
        .create()


    @TypeConverter
    fun fromString(value: String): List<WeatherModel> {
        val listType = object : TypeToken<List<WeatherModel>>() {}.type

        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<WeatherModel>): String {
        return gson.toJson(list)
    }
}