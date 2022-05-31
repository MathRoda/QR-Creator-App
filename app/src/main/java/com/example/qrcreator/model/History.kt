package com.example.qrcreator.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history_table")
data class History (
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "type") val type: String,
        ): Parcelable {


      @PrimaryKey(autoGenerate = true)
          var id: Int = 0
        }