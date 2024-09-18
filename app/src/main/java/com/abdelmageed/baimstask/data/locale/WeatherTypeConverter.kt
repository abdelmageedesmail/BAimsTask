package com.abdelmageed.baimstask.data.locale

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.domain.weather.WeatherType
import com.google.gson.Gson

@ProvidedTypeConverter
class WeatherTypeConverter {

    @TypeConverter
    fun fromWeatherType(weatherType: WeatherType?): String? {
        if (weatherType == null) return null
        return when (weatherType.weatherDesc) {
            WeatherType.Clear.toString() -> "Clear Sky"
            WeatherType.Rain.toString() -> "Rainy"
            WeatherType.Snow.toString() -> "Snowy"
            WeatherType.Cloud.toString() -> "Cloudy"
            WeatherType.Thunderstorm.toString() -> "Thunderstorm"
            else -> "Clear"
        }
    }

    @TypeConverter
    fun toWeatherType(value: String?): WeatherType? {
        if (value == null) return null
        return when (value) {
            "Clear Sky" -> WeatherType.Cloud
            "Rainy" -> WeatherType.Rain
            "Snowy" -> WeatherType.Snow
            "Cloudy" -> WeatherType.Cloud
            "Thunderstorm" -> WeatherType.Thunderstorm
            // ... other subclasses
            else -> throw IllegalArgumentException("Unknown weather type: $value")
        }
    }
}