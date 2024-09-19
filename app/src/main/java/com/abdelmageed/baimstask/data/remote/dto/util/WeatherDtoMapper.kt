package com.abdelmageed.baimstask.data.remote.dto.util

import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.WeatherResponse
import com.abdelmageed.baimstask.domain.weather.WeatherType
import com.abdelmageed.baimstask.extension.convertStringToDate
import java.util.Calendar
import java.util.Date

class WeatherDtoMapper : EntityMapper<WeatherResponse, List<WeatherModel>> {
    override fun mapFromEntity(entity: WeatherResponse): List<WeatherModel> {
        val weatherList = mutableListOf<WeatherModel>()
        entity.list?.forEach {
            weatherList.add(
                WeatherModel(
                    it?.main?.temp,
                    it?.main?.humidity,
                    it?.main?.pressure,
                    it?.wind?.speed,
                    it?.dtTxt,
                    it?.dtTxt?.convertStringToDate()?.let { it1 -> dateToCalendar(it1) },
                    it?.weather?.get(0)?.description,
                    it?.weather?.get(0)?.main?.let { it1 -> WeatherType.getWeatherType(it1) }
                        ?: run { WeatherType.getWeatherType("rain") },
                    it?.main?.tempMin,
                    it?.main?.tempMax,
                    entity.city?.sunrise,
                    entity.city?.sunset
                )
            )
        }
        return weatherList
    }

    fun mapData(entity: WeatherResponse?): List<WeatherModel> {
        return entity?.let { mapFromEntity(it) } ?: emptyList()
    }

    private fun dateToCalendar(date: Date): Calendar? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }
}