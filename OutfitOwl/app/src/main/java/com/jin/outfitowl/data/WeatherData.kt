package com.jin.outfitowl.data

data class WeatherData(
    val temp: Int,// 온도
    val description: String, // 날씨 상태
    val icon: String, // icon
    val time: String // 시간 및 날짜
) {
}
