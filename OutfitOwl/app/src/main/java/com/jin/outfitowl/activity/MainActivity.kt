package com.jin.outfitowl.activity

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.jin.outfitowl.R
import com.jin.outfitowl.adapter.AverageClothesAdapter
import com.jin.outfitowl.adapter.HourWeatherAdapter
import com.jin.outfitowl.adapter.WeeklyWeatherAdapter
import com.jin.outfitowl.data.AverageWeather
import com.jin.outfitowl.data.ClothesTemp
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.jin.outfitowl.manager.OpenWeatherManager
import com.jin.outfitowl.manager.OpenWeatherManager.API_KEY
import com.jin.outfitowl.manager.OpenWeatherManager.API_URL
import com.jin.outfitowl.util.TAG
import com.jin.outfitowl.viewModel.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: ViewModel by viewModels()

    private lateinit var locationManager: LocationManager
    private val storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                        viewModel.insertWeatherData(lat, long, response.toString())
                        val currentWeather =
                            OpenWeatherManager.convertCurrentWeather(response.getJSONObject("current"))
                        binding.tvTemp.text = "${currentWeather.temp}℃"
                        getCurrentClothes(currentWeather.temp)
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
                        getAverageClothes((averageWeather.minTemp + averageWeather.maxTemp) / 2)
                        setWeeklyList(
                            OpenWeatherManager.convertWeeklyWeatherList(
                                response.getJSONArray(
                                    "daily"
                                )
                            )
                        )
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                    Log.e(com.jin.outfitowl.util.TAG.LOCATION.label, "[$statusCode] $errorResponse")
                }
            })
    }

    fun getCurrentClothes(temp: Int) {
        Log.d(TAG.TEST.label, "temp : $temp")
        val clothesTemp = ClothesTemp.findByClothesTemp(temp)
        Log.d(TAG.TEST.label, "clothesTemp : ${clothesTemp.valueName}")
        val forestRef = storageRef.child("images/$clothesTemp/")
        forestRef.listAll().addOnSuccessListener { metadata ->
            for (item in metadata.items) {
                item.downloadUrl.addOnSuccessListener { item ->
                    Log.i(TAG.TEST.label, "metadata.path : ${item}")

                    Glide
                        .with(this@MainActivity)
                        .load(item)
                        .centerCrop()
                        .into(binding.layoutClothes.ivCurrentClothes);

                }

            }
        }
    }

    fun getAverageClothes(temp: Int) {
        val clothesTemp = ClothesTemp.findByClothesTemp(temp)
        val clothsList = ArrayList<String>()
        Log.d(TAG.TEST.label, "clothesTemp : ${clothesTemp.valueName}")
        val forestRef = storageRef.child("images/$clothesTemp/")
        forestRef.listAll().addOnSuccessListener { metadata ->
            for (item in metadata.items) {
                item.downloadUrl.addOnSuccessListener { item ->
                    Log.i(TAG.TEST.label, "metadata.path : ${item}")
                    // list add
                    clothsList.add(item.toString())
                }

            }
            binding.layoutClothes.rvClothes.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = AverageClothesAdapter(clothsList, this@MainActivity)
            }
        }
    }

    fun setWeeklyList(weeklyList: List<AverageWeather>) {
        binding.rvWeekly.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = WeeklyWeatherAdapter(weeklyList, this@MainActivity)
        }
    }
}