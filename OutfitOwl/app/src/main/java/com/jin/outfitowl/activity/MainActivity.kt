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
import com.jin.outfitowl.data.Configuration
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.jin.outfitowl.manager.OpenWeatherManager
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

    private val clothsList = ArrayList<String>()
    private val hourlyList = ArrayList<WeatherData>()
    private val weeklyList = ArrayList<AverageWeather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initLayout()
    }

    override fun onResume() {
        super.onResume()
        getCurrentLocation()
    }

    private fun initLayout() {
        binding.rvHourlyWeather.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = HourWeatherAdapter(hourlyList, this@MainActivity)
        }

        binding.layoutClothes.rvClothes.apply {
            layoutManager =
                LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = AverageClothesAdapter(clothsList, this@MainActivity)
        }

        binding.rvWeekly.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = WeeklyWeatherAdapter(weeklyList, this@MainActivity)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latitude = currentLocation?.latitude ?: 0.0
        val longitude = currentLocation?.longitude ?: 0.0
        fetchWeatherData(latitude, longitude)
    }

    private fun fetchWeatherData(lat: Double, long: Double) {
        val url = "${API_URL}lat=${lat}&lon=${long}&lang=kr&exclude=minutely,alerts&appid=${Configuration.API_KEY}"
        AsyncHttpClient().get(url, object : JsonHttpResponseHandler() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)
                if (response != null) {
                    handleWeatherResponse(response, lat, long)
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

    private fun handleWeatherResponse(response: JSONObject, lat: Double, long: Double) {
        viewModel.insertWeatherData(lat, long, response.toString())
        updateCurrentWeather(response, lat, long)

        // 시간별
        val hourlyWeather = OpenWeatherManager.convertHourlyWeatherList(response.getJSONArray("hourly"))
        if (hourlyList.isNotEmpty()) hourlyList.clear()
        hourlyList.addAll(hourlyWeather)
        binding.rvHourlyWeather.adapter?.notifyDataSetChanged()


        // 요일 별
        val weeklyWeather = OpenWeatherManager.convertWeeklyWeatherList(response.getJSONArray("daily"))
        if (weeklyList.isNotEmpty()) weeklyList.clear()
        weeklyList.addAll(weeklyWeather)
        binding.rvWeekly.adapter?.notifyDataSetChanged()
    }

    private fun updateCurrentWeather(response: JSONObject, lat: Double, long: Double) {
        val currentWeather = OpenWeatherManager.convertCurrentWeather(response.getJSONObject("current"))
        getCurrentClothes(currentWeather.temp)
        binding.tvTemp.text = "${currentWeather.temp}℃"
        binding.tvWeatherDescription.text = currentWeather.description
        binding.tvAddress.text = OpenWeatherManager.getAddress(this@MainActivity, lat, long)
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

        // 현재인데, 평균 값
        val averageWeather = OpenWeatherManager.covertCurrentWeatherAverage(response.getJSONArray("daily"))
        binding.tvAverageDescription.text = averageWeather.summary
        binding.tvAverageTemp.text = averageWeather.averageTemp
        getAverageClothes((averageWeather.minTemp + averageWeather.maxTemp) / 2)
    }

    fun getCurrentClothes(temp: Int) {
        val clothesTemp = ClothesTemp.findByClothesTemp(temp)
        val forestRef = storageRef.child("images/$clothesTemp/")
        forestRef.listAll().addOnSuccessListener { metadata ->
            metadata.items.firstOrNull()?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).centerCrop().into(binding.layoutClothes.ivCurrentClothes)
            }
        }
    }

    fun getAverageClothes(temp: Int) {
        if (clothsList.isNotEmpty()) clothsList.clear()
        val clothesTemp = ClothesTemp.findByClothesTemp(temp)
        val forestRef = storageRef.child("images/$clothesTemp/")
        forestRef.listAll().addOnSuccessListener { metadata ->
            for (item in metadata.items) {
                item.downloadUrl.addOnSuccessListener { uri ->
                    clothsList.add(uri.toString())
                    binding.layoutClothes.rvClothes.adapter?.notifyDataSetChanged()
                }

            }
        }
    }
}