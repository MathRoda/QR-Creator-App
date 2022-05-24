package com.example.qrcreator.data

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qrcreator.model.History


@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQrHistory(history: History)

    @Query("SELECT * FROM history_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<History>>


}