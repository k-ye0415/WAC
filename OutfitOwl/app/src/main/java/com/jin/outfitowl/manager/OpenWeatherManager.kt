package com.jin.outfitowl.manager

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jin.outfitowl.data.AverageWeather
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.util.TAG
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object OpenWeatherManager {
    val API_URL = "https://api.openweathermap.org/data/3.0/onecall?"


    fun getAddress(context: Context, lat: Double, long: Double): String {
        var address = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    lat,
                    long,
                    1
                ) { addressList ->
                    if (addressList != null && addressList.isNotEmpty()) {
                        address = addressList[0].adminArea
                    } else {
                        Log.i(TAG.LOCATION.label, "No city found for this location.")
                    }
                }
            } else {
                val addresses =
                    geocoder.getFromLocation(lat, long, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    address = addresses[0].adminArea
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertCurrentWeather(obj: JSONObject): WeatherData {
        val currentTemp = (obj.getDouble("temp") - 273.15).toInt()
        val weather = obj.getJSONArray("weather").getJSONObject(0)
        val weatherDescription = weather.getString("description")
        val icon = weather.getString("icon")
        return WeatherData(currentTemp, weatherDescription, icon, "")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertHourlyWeatherList(objList: JSONArray): List<WeatherData> {
        val list = ArrayList<WeatherData>()
        for (i in 0 until objList.length()) {
            val hourlyWeather = convertHourlyWeather(objList.getJSONObject(i))
            if (hourlyWeather != null) {
                list.add(hourlyWeather)
            }
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertHourlyWeather(obj: JSONObject): WeatherData? {
        return if (obj != null) {
            val date = obj.getLong("dt")
            val time = convertDate(date)
            val temp = (obj.getDouble("temp") - 273.15).toInt()
            val weather = obj.getJSONArray("weather").getJSONObject(0)
            val weatherDescription = weather.getString("description")
            val icon = weather.getString("icon")
            return WeatherData(temp, weatherDescription, icon, time)
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun covertCurrentWeatherAverage(objList: JSONArray): AverageWeather {
        val today = LocalDate.now(ZoneId.systemDefault())
        for (i in 0 until objList.length()) {
            val daily = objList.getJSONObject(i)
            val weatherDetails = getWeatherDetails(daily)
            val localDate = weatherDetails.localDate
            if (today.isEqual(localDate)) {
                return weatherDetails
            }
        }
        return AverageWeather()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertWeeklyWeatherList(objList: JSONArray): List<AverageWeather> {
        val weeklyList = ArrayList<AverageWeather>()
        val today = LocalDate.now(ZoneId.systemDefault())

        for (i in 0 until objList.length()) {
            val weekly = objList.getJSONObject(i)
            val weatherDetails = getWeatherDetails(weekly)
            if (!today.isEqual(weatherDetails.localDate)) {
                weeklyList.add(weatherDetails)
            }
        }
        return weeklyList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeatherDetails(jsonObject: JSONObject): AverageWeather {
        val timestamp = jsonObject.getLong("dt")
        val localDate = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()

        // 온도 정보 추출 및 섭씨로 변환
        val temp = jsonObject.getJSONObject("temp")
        val min = (temp.getDouble("min") - 273.15).toInt()
        val max = (temp.getDouble("max") - 273.15).toInt()
        val averageTemp = "최저 온도 : $min℃ / 최고 온도 : $max℃"

        // 날씨 정보 추출
        val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
        val description = weather.getString("description")
        val icon = weather.getString("icon")

        // 요일 이름 추출
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("EEE", Locale.getDefault())
        val day = format.format(date)

        return AverageWeather(
            day = day,
            summary = jsonObject.optString("summary", ""),
            minTemp = min,
            maxTemp = max,
            averageTemp = averageTemp,
            description = description,
            icon = icon,
            localDate = localDate
        )
    }

}