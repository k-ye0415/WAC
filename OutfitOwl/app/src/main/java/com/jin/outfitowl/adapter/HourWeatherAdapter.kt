package com.jin.outfitowl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jin.outfitowl.R
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.databinding.ItemHourlyWeatherBinding

class HourWeatherAdapter(private val hourlyList: List<WeatherData>, val context: Context) :
    RecyclerView.Adapter<HourWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourWeatherAdapter.ViewHolder {
        val binding =
            ItemHourlyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourWeatherAdapter.ViewHolder, position: Int) {
        val hourly = hourlyList.get(position)
        holder.binding.tvHour.text = hourly.time
        Glide
            .with(context)
            .load(
                if (hourly.icon.contains("01")) {
                    if (hourly.icon == "01d") {
                        R.drawable.ic_clear_day
                    } else {
                        R.drawable.ic_clear_night
                    }
                } else {
                    "https://openweathermap.org/img/wn/${hourly.icon}@2x.png"
                }
            )
            .centerCrop()
//                        .placeholder(R.drawable.loading_spinner)
            .into(holder.binding.ivWeatherIcon)
        holder.binding.tvTemp.text = hourly.temp
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }

    class ViewHolder(val binding: ItemHourlyWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}
