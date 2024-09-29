package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import database.dao.WeatherDAO

@Database(version = 1, entities = [Weather::class], autoMigrations = [], exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDAO(): WeatherDAO


    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}