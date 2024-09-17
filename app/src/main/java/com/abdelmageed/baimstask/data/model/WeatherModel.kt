package com.abdelmageed.baimstask.data.model

import com.abdelmageed.baimstask.domain.weather.WeatherType
import java.util.Calendar
import java.util.Date

data class WeatherModel(
    val temp: Double? = null,
    val humidity: Double? = null,
    val pressure: Double? = null,
    val windSpeed: Double? = null,
    val time: String? = null,
    val formattedDate: Calendar? = null,
    val windDesc: String? = null,
    val weatherType: WeatherType,
    val minTemp: Double? = null,
    val maxTemp: Double? = null,
    val sunRise: Long? = null,
    val sunSet: Long? = null
)
