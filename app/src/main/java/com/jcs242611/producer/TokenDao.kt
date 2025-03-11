package com.jcs242611.producer

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TokenDao {
    @Insert
    suspend fun insertToken(token: Token): Long

    @Query("SELECT * FROM TokenRepository ORDER BY timestamp DESC LIMIT 4")
    fun getLatestTokens(): Cursor
}