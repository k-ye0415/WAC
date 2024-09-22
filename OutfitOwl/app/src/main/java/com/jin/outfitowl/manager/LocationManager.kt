package com.jin.outfitowl.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jin.outfitowl.data.Permission
import com.loopj.android.http.RequestParams

object LocationManager {
    val API_KEY = "056af9cbd5ca37ebf555e67cdb7e8346"
    val API_URL = "https://api.openweathermap.org/data/3.0/onecall?"
    val MIN_TIME: Long = 5000
    val MIN_DISTANCE: Float = 1000F

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

    fun getCurrentLocation(activity: Activity): RequestParams {
        val params = RequestParams()
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = LocationListener { location ->
            params.put("lat", location.latitude) // 위도
            params.put("lon", location.longitude) // 경도
            params.put("lang", "kr") // 언어
            params.put("exclude", "minutely,alerts") // 불필요 정보
            params.put("appid", API_KEY)
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, MIN_TIME,
            MIN_DISTANCE, locationListener
        )
//        startLocationUpdates(activity, locationManager, locationListener)
        return params
    }

    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 위치 권한 확인 및 요청
    private fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            Permission.REQUEST_CODE_PERMISSION.number
        )
    }

    // 위치 권한 체크 후 사용
    fun startLocationUpdates(
        activity: Activity
    ): RequestParams? {
        return if (checkLocationPermission(activity)) {
            // 권한이 허용된 경우 위치 서비스 사용
            getCurrentLocation(activity)
        } else {
            // 권한이 허용되지 않은 경우 권한 요청
            requestLocationPermission(activity)
            null
        }
    }

}