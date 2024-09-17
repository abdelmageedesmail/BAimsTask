package com.abdelmageed.baimstask.presentation.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelmageed.baimstask.R
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.databinding.ItemHourlyForecastBinding
import com.abdelmageed.baimstask.extension.getTimeFormat
import com.abdelmageed.baimstask.extension.getTimeFormatForTomorrow
import java.util.Calendar

class HourlyForecastAdapter(
    private val list: MutableList<WeatherModel>,
    private val onClick: (WeatherModel) -> Unit
) :
    RecyclerView.Adapter<HourlyForecastAdapter.ServiceViewHolder>() {
    private lateinit var binding: ItemHourlyForecastBinding
    private var selectedItem = 0

    inner class ServiceViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        binding =
            ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(
            binding.root
        )
    }

    fun addItems(items: List<WeatherModel>) {
        list.addAll(items)
        notifyItemInserted(list.size - 1)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (list.size < 8) list.size else 8


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: ServiceViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val weather = list[position]
        if (weather.formattedDate?.let { isSameDay(it) } == false) {
            binding.tvTime.text = weather.time?.getTimeFormatForTomorrow() ?: ""
        } else {
            binding.tvTime.text = weather.time?.getTimeFormat() ?: ""
        }
        if (isNight(
                weather.sunRise ?: 0L,
                weather.sunSet ?: 0L,
                weather.formattedDate ?: Calendar.getInstance()
            )
        ) {
            if (weather.weatherType.weatherDesc.contains("Clear")) {
                binding.ivImage.setIconResource(R.drawable.ic_moon)
            } else {
                binding.ivImage.setIconResource(weather.weatherType.iconRes)
            }
        } else {
            binding.ivImage.setIconResource(weather.weatherType.iconRes)
        }

        binding.tvTemp.text = "${weather.temp} \u2103"

        if (selectedItem == position) {
            binding.ivImage.strokeColor = ColorStateList.valueOf(Color.parseColor("#E1D7C6"))
            binding.ivImage.iconTint = ColorStateList.valueOf(Color.parseColor("#E1D7C6"))
            binding.ivImage.strokeWidth = 1
        } else {
            binding.ivImage.strokeColor = ColorStateList.valueOf(Color.BLACK)
            binding.ivImage.strokeWidth = 1
        }
        binding.ivImage.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            onClick(list[position])
        }

        binding.root.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            onClick(list[position])
        }
        holder.setIsRecyclable(false)
    }

    private fun isSameDay(date: Calendar): Boolean {
        val currentDate = Calendar.getInstance()
        return date.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)
    }

    private fun isNight(sunrise: Long, sunset: Long, calendar: Calendar): Boolean {
        val currentTime = calendar.timeInMillis / 1000  // Current time in seconds
        return currentTime < sunrise || currentTime > sunset
    }

}