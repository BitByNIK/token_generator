package com.jcs242611.producer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TokenRepository")
data class Token(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val timestamp: String,
    val latitude: Double,
    val longitude: Double
)