package com.example.qrcreator.ui

import android.graphics.Bitmap
import android.nfc.Tag
import android.os.Environment
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.load
import com.example.qrcreator.R
import java.io.File
import java.io.IOException

class QrViewModel: ViewModel() {

    private var _textQR = MutableLiveData<String?>("")
    val textQR: LiveData<String?> = _textQR

    private var _selectionList = MutableLiveData<String?>()
    val selectionList: LiveData<String?> = _selectionList

    fun setTextQR(text: String?) {
        _textQR.value = text
    }

    fun setSelectionList(listSel: String) {
        _selectionList.value = listSel
    }

    fun generateQrCode(imageView: ImageView, text: String?) {

        imageView.load("https://api.qrserver.com/v1/create-qr-code/?size=500x500&data=$text") {
            crossfade(true)
            crossfade(500)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_connection_error)
        }
    }


}