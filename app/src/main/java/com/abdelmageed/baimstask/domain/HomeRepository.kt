package com.abdelmageed.baimstask.domain

import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.CitiesItem
import com.abdelmageed.baimstask.data.utils.BaseResult
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface HomeRepository {
    suspend fun getCities(cities: JSONObject?): Flow<MutableList<CitiesItem>>
    suspend fun getWeatherDetails(
        lat: Double,
        lon: Double
    ): Flow<BaseResult<List<WeatherModel>, String>>

}