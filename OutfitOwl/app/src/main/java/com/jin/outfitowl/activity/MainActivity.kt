package com.jin.outfitowl.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jin.outfitowl.R
import com.jin.outfitowl.adapter.HourWeatherAdapter
import com.jin.outfitowl.data.Permission
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.jin.outfitowl.manager.LocationManager.API_KEY
import com.jin.outfitowl.manager.LocationManager.MIN_DISTANCE
import com.jin.outfitowl.manager.LocationManager.MIN_TIME
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val TAG = "yejin"

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    var address: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "oncreate")


        val params = com.jin.outfitowl.manager.LocationManager.getCurrentLocation(this@MainActivity)
        Log.d(TAG, "params 가 안됬어용? $params")
        doNetworking(params)
        binding.rvHourlyWeather.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private fun getCurrentLocation() {
        val params = RequestParams()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            params.put("lat", location.latitude) // 위도
            params.put("lon", location.longitude) // 경도
            params.put("lang", "kr") // 언어
            params.put("exclude", "minutely,alerts") // 불필요 정보
            params.put("appid", API_KEY)
        }

    }

    private fun doNetworking(params: RequestParams) {
        AsyncHttpClient().get(
            com.jin.outfitowl.manager.LocationManager.API_URL,
            params,
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
                            WeatherData.convertCurrentWeather(response.getJSONObject("current"))
                        binding.tvTemp.text = currentWeather.weatherTemp
                        binding.tvWeatherDescription.text = currentWeather.weatherDescription
                        binding.tvAddress.text = address
                        val icon = if (currentWeather.weatherIcon.contains("01")) {
                            if (currentWeather.weatherIcon == "01d") {
                                R.drawable.ic_clear_day
                            } else {
                                R.drawable.ic_clear_night
                            }
                        } else {
                            "https://openweathermap.org/img/wn/${currentWeather.weatherIcon}@2x.png"
                        }
                        Glide
                            .with(this@MainActivity)
                            .load(icon)
                            .centerCrop()
                            .into(binding.ivWeatherIcon);

                        val hourlyList =
                            WeatherData.convertHourlyWeatherList(response.getJSONArray("hourly"))
                        binding.rvHourlyWeather.adapter =
                            HourWeatherAdapter(hourlyList, this@MainActivity)
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                    Log.e(TAG, "statusCode $statusCode, errorResponse : $errorResponse")
                }
            })
    }


    private fun checkPermission() {
        val permissions = Permission.checkBuildPermission(Build.VERSION.SDK_INT)
        val deniedPermissions = permissions.filterNot { Permission.checkPermission(this, it) }
        if (deniedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, deniedPermissions.toTypedArray(),
                Permission.REQUEST_CODE_PERMISSION.number
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permission.REQUEST_CODE_PERMISSION.number) {
            val allPermissionGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionGranted) {
                getCurrentLocation()

                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, MIN_TIME,
                    MIN_DISTANCE, locationListener
                )
            } else {
                Log.e("TAG", "allow permission")
            }
        } else {
            Log.e("TAG", "allow permission")
        }
    }
}