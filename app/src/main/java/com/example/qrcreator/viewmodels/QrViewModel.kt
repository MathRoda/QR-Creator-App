package com.example.qrcreator.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.qrcreator.R


class QrViewModel: ViewModel() {


    // Livedata variables to store the input from user and share it with fragments
    private var _textQR = MutableLiveData<String?>("")
    val textQR: LiveData<String?> = _textQR

    private var _urlRequest = MutableLiveData<String?>("")
    val urlRequest: LiveData<String?> = _urlRequest


    // a setter function that take input value, store it in ViewModel variable
    fun setTextQR(text: String?) {
        _textQR.value = text
    }

    fun setUrlQR(text: String?) {
        _urlRequest.value = text
    }




    // this function uses Coil to send request to QR generator api and form it into bitmap
    suspend fun generateQrCode( text: String?, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(_urlRequest.value + text)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_connection_error)
            .crossfade(true)
            .crossfade(500)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }





}