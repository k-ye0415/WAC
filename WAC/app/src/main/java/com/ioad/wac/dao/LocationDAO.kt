package com.ioad.wac.dao

import androidx.room.*
import com.ioad.wac.model.Location

@Dao
interface LocationDAO {

    @Insert
    fun insertLocation(location:Location)

    @Update
    fun updateLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)


    @Query("SELECT * FROM location")
    fun getAllLocation():List<Location>


}