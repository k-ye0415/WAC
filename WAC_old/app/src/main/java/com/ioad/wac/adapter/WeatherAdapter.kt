package com.ioad.wac.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ioad.wac.R
import com.ioad.wac.model.Weather

class WeatherAdapter(
    val weatherList:Array<Weather>,
    val inflater: LayoutInflater
): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvTime:TextView
        val tvMax:TextView
        val tvMin:TextView
        val tvSky:TextView
        val tvRain:TextView
        val tvNowTemp:TextView
        init {
            tvTime = itemView.findViewById(R.id.tvTime)
            tvMax = itemView.findViewById(R.id.tvMax)
            tvMin = itemView.findViewById(R.id.tvMin)
            tvSky = itemView.findViewById(R.id.tvSky)
            tvRain = itemView.findViewById(R.id.tvRain)
            tvNowTemp = itemView.findViewById(R.id.tvNowTemp)


            tvTime.text = weatherList.get(0).fcstTime
            tvMax.text = weatherList.get(adapterPosition).max
            tvMin.text = weatherList.get(adapterPosition).min
            tvSky.text = getSky(weatherList.get(adapterPosition).sky)
            tvRain.text = weatherList.get(adapterPosition).rain
            tvNowTemp.text = weatherList.get(adapterPosition).now


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.weather_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }


    // 하늘 상태
    fun getSky(sky : String) : String {
        return when(sky) {
            "1" -> "맑음"
            "3" -> "구름 많음"
            "4" -> "흐림"
            else -> "오류 rainType : " + sky
        }
    }
}