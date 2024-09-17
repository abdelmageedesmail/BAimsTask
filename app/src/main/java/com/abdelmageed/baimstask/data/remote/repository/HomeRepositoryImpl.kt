package com.abdelmageed.baimstask.data.remote.repository

import android.util.Log
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
    private val apiKey: String
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
}