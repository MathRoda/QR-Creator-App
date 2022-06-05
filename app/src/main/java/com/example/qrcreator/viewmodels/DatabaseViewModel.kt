package com.example.qrcreator.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qrcreator.data.HistoryDatabase
import com.example.qrcreator.model.History
import com.example.qrcreator.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class DatabaseViewModel(application: Application): AndroidViewModel(application) {

   val readAllData: LiveData<List<History>>
   private val repository: HistoryRepository


   init {
       val historyDao = HistoryDatabase.getDatabase(application).historyDao()
       repository = HistoryRepository(historyDao)
       readAllData= repository.readAllData
   }


    fun addQrHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQrHistory(history)
        }
    }

    fun deleteQrHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQrHistory(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllHistory()
        }
    }



}