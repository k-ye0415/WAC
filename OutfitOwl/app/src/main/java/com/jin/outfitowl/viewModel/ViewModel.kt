package com.jin.outfitowl.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import database.Weather
import database.repository.WeatherRepository

class ViewModel(application: Application) : AndroidViewModel(application) {


    fun insertWeatherData(latitude: Double, longitude: Double, jsonString: String) {
        val latStr = String.format("%.2f", latitude)
        val longStr = String.format("%.2f", longitude)
        val weather = Weather(latStr, longStr, jsonString)
        WeatherRepository.insertWeatherData(weather)
    }

}