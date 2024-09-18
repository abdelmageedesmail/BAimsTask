package com.abdelmageed.baimstask.presentation.home

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.data.remote.dto.response.CitiesItem
import com.abdelmageed.baimstask.databinding.FragmentHomeBinding
import com.abdelmageed.baimstask.extension.getCurrentDate
import com.abdelmageed.baimstask.extension.gone
import com.abdelmageed.baimstask.extension.isOnline
import com.abdelmageed.baimstask.extension.previewFromStoredDataDialog
import com.abdelmageed.baimstask.extension.showToast
import com.abdelmageed.baimstask.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var cityName: String? = null
    private lateinit var hourlyForecastAdapter: HourlyForecastAdapter
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        readDataFromAssets()
        viewModel.getCities(readDataFromAssets()?.let { JSONObject(it) })
        binding.apply {
            tvDateTime.text = Date().getCurrentDate()
            hourlyForecastAdapter = HourlyForecastAdapter(mutableListOf()) {
                tvWind.text = "${it.windSpeed} km/h"
                tvHumidity.text = "${it.humidity}%"
                tvWeatherStatus.text = "${it.windDesc}"
            }
            rvHourlyForeCast.adapter = hourlyForecastAdapter
            rvHourlyForeCast.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

            btnRetry.setOnClickListener {
                readDataFromAssets()
            }


        }
    }

    private fun observer() {
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: HomeFragmentState) {
        when (state) {
            is HomeFragmentState.SuccessGetCities -> handleGetCities(state.cities)
            is HomeFragmentState.Error -> {
                requireActivity().showToast(state.message)
                previewFromStoredDataDialog {
                    if (it == 1) {
                        viewModel.getWeatherDetailsFromDb(cityName ?: "")
                    } else {
                        binding.frError.visible()
                        binding.cardHourlyForeCast.gone()
                    }
                }
            }

            is HomeFragmentState.SuccessAddedToDb -> {
                Log.e("added", "addedSuccessfully")
            }

            is HomeFragmentState.SuccessGetWeatherDetails -> updateWeatherDetails(state.weatherResponse)
            is HomeFragmentState.IsLoading -> showLoading(state.isLoading)
            is HomeFragmentState.Init -> Unit
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.cardHourlyForeCast.gone()
            binding.frProgress.visible()
        } else {
            binding.cardHourlyForeCast.visible()
            binding.frProgress.gone()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherDetails(weatherResponse: List<WeatherModel>?) {
        Log.e("weatherDataList", "$weatherResponse")
        binding.cardHourlyForeCast.visible()
        weatherResponse?.let {
            if (weatherResponse.isNotEmpty()) {
                viewModel.saveWeatherDetailsInDb(
                    cityName ?: "",
                    weatherResponse
                )
                binding.apply {
                    tvWind.text = "${weatherResponse[0].windSpeed} km/h"
                    tvHumidity.text = "${weatherResponse[0].humidity}%"
                    tvWeatherStatus.text = "${weatherResponse[0].windDesc}"
                    tvTemp.text = "${weatherResponse[0].temp} ℃"
                    tvStatus.text = weatherResponse[0].weatherType.weatherDesc
                    if (::hourlyForecastAdapter.isInitialized) {
                        hourlyForecastAdapter.clear()
                    }
                    hourlyForecastAdapter.addItems(weatherResponse.toMutableList())

                    val firstIndexOfDateList = viewModel.getFirstIndexOfDateList(weatherResponse)

                    rvWeather.adapter =
                        DailyForecastAdapter(firstIndexOfDateList.values.toMutableList()) {

                            tvWind.text = "${it.windSpeed} km/h"
                            tvHumidity.text = "${it.humidity}%"
                            tvWeatherStatus.text = "${it.windDesc}"
                            tvTemp.text = "${it.temp} ℃"
                            tvStatus.text = it.weatherType.weatherDesc

                            hourlyForecastAdapter.clear()
                            hourlyForecastAdapter.addItems(
                                viewModel.getDayWeather(
                                    weatherResponse,
                                    it.formattedDate?.get(Calendar.DAY_OF_MONTH) ?: 0
                                )
                            )
                        }
                    rvWeather.layoutManager =
                        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                }
            } else {
                requireActivity().showToast("No Weather Data Available")
            }
        }
    }

    private fun handleGetCities(cities: MutableList<CitiesItem>) {
        Log.e("cities", cities.toString())
        val citiesAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_list_item_1,
            cities
        )
        binding.spCities.adapter = citiesAdapter
        binding.spCities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // You can define you actions as you want
                }

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    ((p0?.getChildAt(0)) as TextView).setTextColor(
                        resources.getColor(
                            R.color.white,
                            null
                        )
                    )
                    val selectedObject = binding.spCities.selectedItem as CitiesItem
                    cityName = selectedObject.cityNameEn
                    if (requireActivity().isOnline()) {
                        viewModel.getWeatherDetails(
                            selectedObject.lat ?: 0.0,
                            selectedObject.lon ?: 0.0
                        )
                    } else {
                        previewFromStoredDataDialog {
                            if (it == 1) {
                                viewModel.getWeatherDetailsFromDb(cityName ?: "")
                            } else {
                                binding.frError.visible()
                                binding.cardHourlyForeCast.gone()
                            }
                        }

                    }


                }
            }

    }

    private fun readDataFromAssets(): String? {
        return try {
            val inputStream = requireActivity().assets.open("cities_json.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}