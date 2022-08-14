package com.ioad.wac.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ioad.wac.database.LocationDAO
import com.ioad.wac.database.Location

@Database(entities = [Location::class], version = 2)
abstract class LocationDB:RoomDatabase() {

    abstract fun locationDAO(): LocationDAO


//    companion object {
//        private var instance:LocationDB? = null
//
//        @Synchronized
//        fun getInstance(context: Context): LocationDB? {
//            if (instance == null) {
//                synchronized(LocationDB::class) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        LocationDB::class.java,
//                        "location_database"
//                    ).build()
//                }
//            }
//            return instance
//        }
//
//    }


}