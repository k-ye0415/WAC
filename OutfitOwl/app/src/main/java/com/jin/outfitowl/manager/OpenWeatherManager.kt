package com.jin.outfitowl.manager

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import java.util.Locale

object OpenWeatherManager {
    val API_KEY = "056af9cbd5ca37ebf555e67cdb7e8346"
    val API_URL = "https://api.openweathermap.org/data/3.0/onecall?"

    //    val API_URL = "https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=minutely,alerts&appid=$API_KEY"
//    https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid={API key}
    val MIN_TIME: Long = 5000
    val MIN_DISTANCE: Float = 1000F


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
                        address =
                            "${addressList[0].adminArea} ${if (addressList[0].thoroughfare != "null") addressList[0].thoroughfare else ""}"
                        Log.i(
                            "TAG",
                            "City: ${addressList[0].adminArea}, ${addressList[0].thoroughfare}"
                        )
                    } else {
                        Log.i("TAG", "No city found for this location.")
                    }
                }
            } else {
                val addresses =
                    geocoder.getFromLocation(lat, long, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    address =
                        "${addresses[0].adminArea} ${if (addresses[0].thoroughfare != "null") addresses[0].thoroughfare else ""}"
                    Log.i(
                        "TAG",
                        "City: $address"
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }
}