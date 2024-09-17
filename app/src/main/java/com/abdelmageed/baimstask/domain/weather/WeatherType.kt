package com.abdelmageed.baimstask.domain.weather

import androidx.annotation.DrawableRes
import com.abdelmageed.baimstask.R

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int
) {
    data object Clear : WeatherType(weatherDesc = "Clear Sky", iconRes = R.drawable.ic_sunny)
    data object Rain : WeatherType(weatherDesc = "Rainy", iconRes = R.drawable.ic_drop_water)
    data object Snow : WeatherType(weatherDesc = "Snowy", iconRes = R.drawable.ic_snow)
    data object Cloud : WeatherType(weatherDesc = "Cloudy", iconRes = R.drawable.ic_cloudy)
    data object Thunderstorm :
        WeatherType(weatherDesc = "Thunderstorm", iconRes = R.drawable.ic_thunderstorm)

    companion object {
        fun getWeatherType(weatherStatus: String): WeatherType {
            return if (weatherStatus.contains("Cloud")) {
                Cloud
            } else if (weatherStatus.contains("Rain")) {
                Rain
            } else if (weatherStatus.contains("Snow")) {
                Snow
            } else if (weatherStatus.contains("Thunderstorm")) {
                Thunderstorm
            } else if (weatherStatus.contains("Clear")) {
                Clear
            } else {
                Clear
            }
        }

    }

}