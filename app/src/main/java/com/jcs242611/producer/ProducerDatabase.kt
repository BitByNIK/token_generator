package com.jcs242611.producer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Token::class], version = 1, exportSchema = false)
abstract class ProducerDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var INSTANCE: ProducerDatabase? = null

        fun getDatabase(context: Context): ProducerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProducerDatabase::class.java,
                    "producer_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}