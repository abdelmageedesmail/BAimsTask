package com.abdelmageed.baimstask.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdelmageed.baimstask.R
import com.abdelmageed.baimstask.data.model.WeatherModel
import com.abdelmageed.baimstask.databinding.ItemDailyForecastBinding
import com.abdelmageed.baimstask.extension.getDayMonthFormat
import com.abdelmageed.baimstask.extension.getNameOfTheDay

class DailyForecastAdapter(
    private val list: MutableList<WeatherModel>,
    private val onClick: (WeatherModel) -> Unit
) :
    RecyclerView.Adapter<DailyForecastAdapter.ServiceViewHolder>() {
    private lateinit var binding: ItemDailyForecastBinding
    private var selectedItem = 0

    inner class ServiceViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        binding =
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(
            binding.root
        )
    }

    override fun getItemCount(): Int = list.size


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

        binding.apply {
            tvDate.text = weather.time?.getDayMonthFormat()
            tvDay.text = weather.time?.getNameOfTheDay()
            tvTemp.text = "${weather.minTemp} ℃ / ${weather.maxTemp}℃"
            root.setOnClickListener {
                selectedItem = position
                notifyDataSetChanged()
                onClick(list[position])
            }
            if (selectedItem == position) {
                frame.setBackgroundResource(R.drawable.shape_background)
            } else {
                frame.setBackgroundResource(0)
            }

        }


        holder.setIsRecyclable(false)
    }
}