package com.ioad.wac.database

import androidx.room.*

@Dao
interface LocationDAO {

    @Insert
    fun insertLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("SELECT * FROM location")
    fun getAllLocation(): List<Location>


    @Query("SELECT * FROM location WHERE delete_status = 'N'")
    fun getRecentLocationList(): List<Location>

    @Query("UPDATE Location SET delete_status = :deleteStatus AND delete_date = :deleteDate WHERE id = :id")
    fun updateRecentLocationDelete(deleteStatus: String, deleteDate: String, id: Int)

    @Query("UPDATE Location SET bookmark = :bookmark WHERE id = :id")
    fun updateRecentBookmark(bookmark: String, id: Int)


}