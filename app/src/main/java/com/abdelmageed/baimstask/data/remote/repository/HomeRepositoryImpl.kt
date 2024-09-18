package com.abdelmageed.baimstask.data.remote.repository

import android.util.Log
import com.abdelmageed.baimstask.data.locale.WeatherDao
import com.abdelmageed.baimstask.data.locale.WeatherDbModel
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.CitiesItem
import com.abdelmageed.baimstask.data.remote.dto.response.WeatherResponse
import com.abdelmageed.baimstask.data.remote.dto.util.WeatherDtoMapper
import com.abdelmageed.baimstask.data.utils.BaseResult
import com.abdelmageed.baimstask.domain.HomeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.lang.reflect.Type
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val dao: WeatherDao
) : HomeRepository {
    override suspend fun getCities(cities: JSONObject?): Flow<MutableList<CitiesItem>> = flow {
        val array = cities?.getJSONArray("cities")
        val gson = Gson()
        val listType: Type = object : TypeToken<MutableList<CitiesItem?>?>() {}.type
        val gsonList: MutableList<CitiesItem> = gson.fromJson(array.toString(), listType)
        emit(gsonList)
    }

    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double
    ): Flow<BaseResult<List<WeatherModel>, String>> = flow {
        try {
            val response = httpClient.get {
                url("forecast?lat=$lat&lon=$lon&appid=${apiKey}&units=metric")
            }
            Log.e("CodeStatus", "${response.status.value}")
            when (response.status.value) {
                200 -> {
                    val type =
                        object : TypeToken<WeatherResponse>() {}.type
                    val res: WeatherResponse =
                        Gson().fromJson(response.bodyAsText(), type)
                    emit(BaseResult.Success(WeatherDtoMapper().mapData(res)))
                }

                else -> {
                    emit(BaseResult.Error("Something went wrong"))
                }
            }

        } catch (e: Exception) {
            emit(BaseResult.Error(e.message.toString()))
        }
    }

    override suspend fun saveWeatherDetails(
        cityName: String,
        list: List<WeatherModel>
    ): Flow<BaseResult<Boolean, String>> = flow {
        try {
            dao.insertForecast(WeatherDbModel(0, cityName, list))
            emit(BaseResult.Success(true))
        } catch (e: Exception) {
            emit(BaseResult.Error(e.message.toString()))
        }
    }

    override suspend fun getWeatherData(cityName: String): Flow<BaseResult<List<WeatherModel>, String>> =
        flow {
            try {
                val response = dao.getForecastForCity(cityName)
                Log.e("responseFromDb", "$response")
                if (response != null) {
                    emit(BaseResult.Success(response.conditions))
                } else {
                    emit(BaseResult.Success(emptyList()))
                }
            } catch (e: Exception) {
                emit(BaseResult.Error(e.message.toString()))
            }
        }
}
