package com.ioad.wac.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    var location:String,
    var bookmark:Boolean

) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}