package com.example.qrcreator.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history_table")
data class History (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String
        ): Parcelable