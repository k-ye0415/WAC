package com.jin.outfitowl.activity

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jin.outfitowl.R
import com.jin.outfitowl.adapter.HourWeatherAdapter
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.jin.outfitowl.manager.OpenWeatherManager
import com.jin.outfitowl.manager.OpenWeatherManager.API_KEY
import com.jin.outfitowl.manager.OpenWeatherManager.API_URL
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val TAG = "yejin"

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "oncreate")
        binding.rvHourlyWeather.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latitude = currentLocation?.latitude ?: 0.0
        val longitude = currentLocation?.longitude ?: 0.0
        doNetworking(latitude, longitude)
    }

    private fun doNetworking(lat: Double, long: Double) {
        val url =
            "${API_URL}lat=${lat}&lon=${long}&lang=kr&exclude=minutely,alerts&appid=${API_KEY}"
        AsyncHttpClient().get(
            url,
            object : JsonHttpResponseHandler() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out cz.msebera.android.httpclient.Header>?,
                    response: JSONObject?
                ) {
                    super.onSuccess(statusCode, headers, response)
                    if (response != null) {
                        val currentWeather =
                            OpenWeatherManager.convertCurrentWeather(response.getJSONObject("current"))
                        binding.tvTemp.text = currentWeather.temp
                        binding.tvWeatherDescription.text = currentWeather.description
                        binding.tvAddress.text =
                            OpenWeatherManager.getAddress(this@MainActivity, lat, long)
                        val icon = if (currentWeather.icon.contains("01")) {
                            if (currentWeather.icon == "01d") {
                                R.drawable.ic_clear_day
                            } else {
                                R.drawable.ic_clear_night
                            }
                        } else {
                            "https://openweathermap.org/img/wn/${currentWeather.icon}@2x.png"
                        }
                        Glide
                            .with(this@MainActivity)
                            .load(icon)
                            .centerCrop()
                            .into(binding.ivWeatherIcon);

                        val hourlyList =
                            OpenWeatherManager.convertHourlyWeatherList(response.getJSONArray("hourly"))
                        binding.rvHourlyWeather.adapter =
                            HourWeatherAdapter(hourlyList, this@MainActivity)
                        val averageWeather =
                            OpenWeatherManager.convertDailyWeatherList(response.getJSONArray("daily"))
                        binding.tvAverageDescription.text = averageWeather.summary
                        binding.tvAverageTemp.text = averageWeather.averageTemp
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                    Log.e(TAG, "[$statusCode] $errorResponse")
                }
            })
    }
}