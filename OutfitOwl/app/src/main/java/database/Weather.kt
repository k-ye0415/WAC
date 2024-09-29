package database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "WeatherTable", primaryKeys = ["latitude", "longitude"])
data class Weather(
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "weatherJson")
    val weatherJson: String
) {

}