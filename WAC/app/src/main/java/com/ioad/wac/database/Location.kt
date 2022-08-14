package com.ioad.wac.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Location(

    // 등록날짜 / 삭제날짜 / 삭제 여

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "bookmark")
    var bookmark: String,

    @ColumnInfo(name = "save_date")
    var saveDate: String,

    @ColumnInfo(name = "delete_status")
    var deleteStatus: String,

    @ColumnInfo(name = "delete_date")
    var deleteDate: String
) {
    constructor(
        location: String,
        bookmark: String,
        saveDate: String,
        deleteStatus: String,
        deleteDate: String
    ) : this(0, location, bookmark, saveDate, deleteStatus, deleteDate)
}