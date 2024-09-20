package com.ioad.wac.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ioad.wac.database.LocationDAO
import com.ioad.wac.database.Location

@Database(entities = [Location::class], version = 2)
abstract class LocationDB:RoomDatabase() {

    abstract fun locationDAO(): LocationDAO
}