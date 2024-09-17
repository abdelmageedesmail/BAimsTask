package com.abdelmageed.baimstask.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.CitiesItem
import com.abdelmageed.baimstask.data.utils.BaseResult
import com.abdelmageed.baimstask.domain.InvokeHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val invokeHomeUseCase: InvokeHomeUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<HomeFragmentState>(HomeFragmentState.Init)
    val state get() = _state

    fun getCities(cities: JSONObject?) {
        viewModelScope.launch {
            invokeHomeUseCase.getCities(cities).catch {
                _state.value = HomeFragmentState.Error(it.message.toString())
            }.collect {
                _state.value = HomeFragmentState.SuccessGetCities(it)
            }
        }
    }

    fun getWeatherDetails(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.value = HomeFragmentState.IsLoading(true)
            invokeHomeUseCase.getWeatherDetails(lat, lon).catch {
                _state.value = HomeFragmentState.IsLoading(false)
                _state.value = HomeFragmentState.Error(it.message.toString())
                Log.e("ErrorDetails", it.message.toString())
            }.collect {
                _state.value = HomeFragmentState.IsLoading(false)
                when (it) {
                    is BaseResult.Success -> _state.value =
                        HomeFragmentState.SuccessGetWeatherDetails(it.data)

                    is BaseResult.Error -> _state.value = HomeFragmentState.Error(it.error)
                }
            }
        }
    }

    private fun isSameDay(date: Calendar, dayOfTheMonth: Int): Boolean {
        return date.get(Calendar.DAY_OF_MONTH) == dayOfTheMonth
    }

    fun getDayWeather(list: List<WeatherModel>, dayOfTheMonth: Int): List<WeatherModel> {
        return list.filter {
            it.formattedDate?.let { it1 ->
                isSameDay(
                    it1,
                    dayOfTheMonth
                )
            } == true
        }
    }

    fun getFirstIndexOfDateList(list: List<WeatherModel>): HashMap<Int, WeatherModel> {
        val map = HashMap<Int, WeatherModel>()
        list.forEachIndexed { _, weatherModel ->
            if (!map.containsKey(weatherModel.formattedDate?.get(Calendar.DAY_OF_MONTH))) {
                val day = weatherModel.formattedDate?.get(Calendar.DAY_OF_MONTH)
                day?.let { map.put(it, weatherModel) }
            }
        }
        return map
    }
}

sealed class HomeFragmentState {
    data object Init : HomeFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeFragmentState()
    data class SuccessGetCities(val cities: MutableList<CitiesItem>) : HomeFragmentState()
    data class SuccessGetWeatherDetails(val weatherResponse: List<WeatherModel>?) :
        HomeFragmentState()

    data class Error(val message: String) : HomeFragmentState()
}