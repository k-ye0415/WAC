package com.jin.outfitowl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.jin.outfitowl.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import org.json.JSONObject
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val apiKey = "f3e6195afa7c7409b5a884619d0f94c8"
    val url =
        "https://api.openweathermap.org/data/2.5/weather?"

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
    }


    private fun getWeatherInCurrentLocation() {
        Log.d(TAG, "getWeatherInCurrentLocation")
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = LocationListener { location ->
            val params: RequestParams = RequestParams()
            params.put("lat", location.latitude)
            params.put("lon", location.longitude)
            params.put("appid", apiKey)
            doNetworking(params)
            // Geocoder를 사용하여 위도, 경도를 주소로 변환
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ) { addressList ->
                        if (addressList != null && addressList.isNotEmpty()) {
                            Log.e(TAG, "anjwl? $addressList")
                            address =
                                "${addressList[0].adminArea} ${if (addressList[0].thoroughfare != "null") addressList[0].thoroughfare else ""}"
                            Log.i(
                                TAG,
                                "City: ${addressList[0].adminArea}, ${addressList[0].thoroughfare}"
                            )
                        } else {
                            Log.i(TAG, "No city found for this location.")
                        }
                    }
                } else {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        address =
                            "${addresses[0].adminArea} ${if (addresses[0].thoroughfare != "null") addresses[0].thoroughfare else ""}"
                        Log.i(
                            TAG,
                            "City: $address"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
        Log.d(TAG, "doNetworking $url")
        AsyncHttpClient().get(url, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out cz.msebera.android.httpclient.Header>?,
                response: JSONObject?
            ) {
                super.onSuccess(statusCode, headers, response)
                Log.d(TAG, "onSuccess response : $response")
                if (response != null) {
                    val weatherId =
                        response.getJSONArray("weather").getJSONObject(0)?.getInt("id") ?: 0
                    val weatherType =
                        response.getJSONArray("weather").getJSONObject(0).getString("main")
                    val icon = response.getJSONArray("weather").getJSONObject(0).getString("icon")
                    val weatherDescription = updateWeatherIcon(weatherId)
                    val roundedTemp: Int =
                        (response.getJSONObject("main").getDouble("temp") - 273.15).toInt()
                    Log.d(
                        TAG,
                        "weatherId : $weatherId, weatherType : $weatherType, weatherIcon : $icon, roundedTemp : $roundedTemp"
                    )
                    binding.tvTemp.text = "$roundedTemp\u2103"
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
//                    코드 500의 경우 - 가벼운 비 아이콘 = "10d". 아래 코드의 전체 목록을 참조하십시오
//                    URL은 https://openweathermap.org/img/wn/10d@2x.png입니다.
                }
            }
        })
    }

    private fun updateWeatherIcon(condition: Int): String {
        return when (condition) {
            in 200..299 -> "뇌우"  // thunderstorm
            in 300..499 -> "이슬비"  // lightrain
            in 500..599 -> "비"  // rain
            in 600..700 -> "눈"  // snow
            in 701..771 -> "안개"  // fog
            in 772..799 -> "흐림"  // overcast
            800 -> "맑음"  // clear
            in 801..804 -> "구름"  // cloudy
            in 900..902 -> "뇌우"  // thunderstorm
            903 -> "눈"  // snow
            904 -> "맑음"  // clear
            in 905..1000 -> "뇌우"  // thunderstorm
            else -> "알 수 없음"  // dunno
        }
    }
}