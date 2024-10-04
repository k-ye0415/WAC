package database.repository

import com.jin.outfitowl.core.Application
import database.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object WeatherRepository {
    private val db by lazy { Application.instance.database }
    private val weatherDAO = db.getWeatherDAO()

    fun insertWeatherData(weather: Weather) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDAO.insert(weather)
        }
    }
}