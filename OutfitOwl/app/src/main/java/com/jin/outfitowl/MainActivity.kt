package com.jin.outfitowl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jin.outfitowl.adapter.HourWeatherAdapter
import com.jin.outfitowl.data.WeatherData
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val apiKey = "056af9cbd5ca37ebf555e67cdb7e8346"
    val url = "https://api.openweathermap.org/data/3.0/onecall?"
    val MIN_TIME: Long = 5000
    val MIN_DISTANCE: Float = 1000F
    val WEATHER_REQUEST: Int = 102
    val TAG = "yejin"

    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener
    var address: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "oncreate")
        getWeatherInCurrentLocation()
        binding.btnRefresh.setOnClickListener {
            getWeatherInCurrentLocation()
        }

        binding.rvHourlyWeather.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun getWeatherInCurrentLocation() {
        Log.d(TAG, "getWeatherInCurrentLocation")
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = LocationListener { location ->
            val params: RequestParams = RequestParams()
            params.put("lat", location.latitude)
            params.put("lon", location.longitude)
            params.put("lang", "kr")
            params.put("exclude", "minutely,alerts")
            params.put("appid", apiKey)
            doNetworking(params)
            // Geocoder를 사용하여 위도, 경도를 주소로 변환
//            val geocoder = Geocoder(this, Locale.getDefault())
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    geocoder.getFromLocation(
//                        location.latitude,
//                        location.longitude,
//                        1
//                    ) { addressList ->
//                        if (addressList != null && addressList.isNotEmpty()) {
//                            Log.e(TAG, "anjwl? $addressList")
//                            address =
//                                "${addressList[0].adminArea} ${if (addressList[0].thoroughfare != "null") addressList[0].thoroughfare else ""}"
//                            Log.i(
//                                TAG,
//                                "City: ${addressList[0].adminArea}, ${addressList[0].thoroughfare}"
//                            )
//                        } else {
//                            Log.i(TAG, "No city found for this location.")
//                        }
//                    }
//                } else {
//                    val addresses =
//                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                    if (addresses != null && addresses.isNotEmpty()) {
//                        address =
//                            "${addresses[0].adminArea} ${if (addresses[0].thoroughfare != "null") addresses[0].thoroughfare else ""}"
//                        Log.i(
//                            TAG,
//                            "City: $address"
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME,
            MIN_DISTANCE,
            mLocationListener
        )
    }

    private fun doNetworking(params: RequestParams) {
        Log.d(TAG, "doNetworking $url, $params")
        AsyncHttpClient().get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)
                Log.d(TAG, "onSuccess response : $response")
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                val formattedDate = sdf.format(date)
                if (response != null) {
                    val currentWeather = response.getJSONObject("current")
                    val currentTemp: Int = (currentWeather.getDouble("temp") - 273.15).toInt()
                    val weather = currentWeather.getJSONArray("weather").getJSONObject(0)
                    val weatherDescription = weather.getString("description")
                    val icon = weather.getString("icon")
                    binding.tvTemp.text = "$currentTemp\u2103"
                    binding.tvWeatherDescription.text = weatherDescription
                    binding.tvAddress.text = address
                    Glide
                        .with(this@MainActivity)
                        .load(
                            if (icon.contains("01")) {
                                if (icon == "01d") {
                                    R.drawable.ic_clear_day
                                } else {
                                    R.drawable.ic_clear_night
                                }
                            } else {
                                "https://openweathermap.org/img/wn/$icon@2x.png"
                            }
                        )
                        .centerCrop()
//                        .placeholder(R.drawable.loading_spinner)
                        .into(binding.ivWeatherIcon);

                    val hourlyJsonList = response.getJSONArray("hourly")
                    val hourlyList = ArrayList<WeatherData>()
                    for (i in 0 until hourlyJsonList.length()) {
                        val hourly = hourlyJsonList.getJSONObject(i)
                        val hourlyDate = hourly.getLong("dt")
                        val dateTime = Instant.ofEpochSecond(hourlyDate)
                        val today = LocalDate.now(ZoneId.systemDefault())
                        val hourlyLocalDate = dateTime.atZone(ZoneId.systemDefault()).toLocalDate()
                        val formatter = if (today.isEqual(hourlyLocalDate)) {
                            DateTimeFormatter.ofPattern("HH")
                        } else {
                            DateTimeFormatter.ofPattern("MM-dd HH")
                        }
                        val formattedDate = dateTime.atZone(ZoneId.systemDefault()).format(formatter)
                        val temp: Int = (hourly.getDouble("temp") - 273.15).toInt()
                        val weather = hourly.getJSONArray("weather").getJSONObject(0)
                        val weatherDescription = weather.getString("description")
                        val icon = weather.getString("icon")
                        hourlyList.add(
                            WeatherData(
                                "$temp\u2103",
                                weatherDescription,
                                icon,
                                formattedDate
                            )
                        )
                    }

                    binding.rvHourlyWeather.adapter = HourWeatherAdapter(hourlyList, this@MainActivity)
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
}