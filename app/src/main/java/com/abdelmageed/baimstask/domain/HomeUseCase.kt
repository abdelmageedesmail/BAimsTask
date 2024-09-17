package com.abdelmageed.baimstask.domain

import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.CitiesItem
import com.abdelmageed.baimstask.data.utils.BaseResult
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import javax.inject.Inject

class InvokeHomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun getCities(cities: JSONObject?): Flow<MutableList<CitiesItem>> {
        return homeRepository.getCities(cities)
    }

    suspend fun getWeatherDetails(
        lat: Double,
        lon: Double
    ): Flow<BaseResult<List<WeatherModel>, String>> {
        return homeRepository.getWeatherDetails(lat, lon)
    }

}