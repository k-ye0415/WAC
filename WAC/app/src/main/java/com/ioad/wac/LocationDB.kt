package com.ioad.wac

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ioad.wac.dao.LocationDAO
import com.ioad.wac.model.Location

@Database(entities = [Location::class], version = 1)
abstract class LocationDB:RoomDatabase() {

    abstract fun locationDAO():LocationDAO


    companion object {
        private var instance:LocationDB? = null

        @Synchronized
        fun getInstance(context: Context): LocationDB? {
            if (instance == null) {
                synchronized(LocationDB::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocationDB::class.java,
                        "location_database"
                    ).build()
                }
            }
            return instance
        }

    }


}