package com.example.qrcreator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.qrcreator.converters.Converters
import com.example.qrcreator.model.History
import kotlinx.coroutines.InternalCoroutinesApi


@Database(entities = [History::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    @InternalCoroutinesApi
    companion object {

        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                "history_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}