package com.jin.outfitowl.data

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class WeatherData(
    val temp: String,// 온도
    val description: String, // 날씨 상태
    val icon: String, // icon
    val time: String // 시간 및 날짜
) {
    @RequiresApi(Build.VERSION_CODES.O)
    companion object {
        fun convertCurrentWeather(obj: JSONObject): WeatherData {
            val currentTemp = "${(obj.getDouble("temp") - 273.15).toInt()}\u2103"
            val weather = obj.getJSONArray("weather").getJSONObject(0)
            val weatherDescription = weather.getString("description")
            val icon = weather.getString("icon")
            return WeatherData(currentTemp, weatherDescription, icon, "")
        }

        fun convertHourlyWeatherList(objList: JSONArray): List<WeatherData> {
            val list = ArrayList<WeatherData>()
            for (i in 0 until objList.length()) {
                val hourlyWeather = convertHourlyWeather(objList.getJSONObject(i))
                list.add(hourlyWeather)
            }
            return list
        }


        fun convertHourlyWeather(obj: JSONObject): WeatherData {
            val date = obj.getLong("dt")
            val time = convertDate(date)
            val temp = "${(obj.getDouble("temp") - 273.15).toInt()}\u2103"
            val weather = obj.getJSONArray("weather").getJSONObject(0)
            val weatherDescription = weather.getString("description")
            val icon = weather.getString("icon")
            return WeatherData(temp, weatherDescription, icon, time)
        }


        fun convertDate(date: Long): String {
            val dateTime = Instant.ofEpochSecond(date)
            val today = LocalDate.now(ZoneId.systemDefault())
            val localDate = dateTime.atZone(ZoneId.systemDefault()).toLocalDate()
            val formatter = if (today.isEqual(localDate)) {
                DateTimeFormatter.ofPattern("HH")
            } else {
                DateTimeFormatter.ofPattern("MM-dd HH")
            }
            return dateTime.atZone(ZoneId.systemDefault()).format(formatter)
        }
    }
}
