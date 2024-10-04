package com.jin.outfitowl.data

data class AverageWeather(
    val day: String = "",
    val summary: String = "",
    val minTemp: Int = 0,
    val maxTemp: Int = 0,
    val averageTemp: String = "",
    val description: String = "",
    val icon: String = ""
) {
}