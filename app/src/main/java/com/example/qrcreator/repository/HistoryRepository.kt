package com.example.qrcreator.repository

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.example.qrcreator.data.HistoryDao
import com.example.qrcreator.model.History

class HistoryRepository(private val historyDao: HistoryDao) {

    val readAllData: LiveData<List<History>> = historyDao.readAllData()



    suspend fun addQrHistory(history: History) {
        historyDao.addQrHistory(history)
    }
}