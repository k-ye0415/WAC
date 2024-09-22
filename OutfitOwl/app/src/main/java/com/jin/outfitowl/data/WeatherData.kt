package com.jin.outfitowl.data

import org.json.JSONException
import org.json.JSONObject

data class WeatherData(
    val weatherTemp: String,// 온도
    val weatherDescription: String, // 날씨 상태
    val weatherIcon: String, // icon
    val weatherTime:String // 시간 및 날짜
) {
}
