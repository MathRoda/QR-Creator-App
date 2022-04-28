package com.example.qrcreator.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.qrcreator.R
import java.io.File
import java.io.FileOutputStream

class QrViewModel: ViewModel() {

    // Livedata variables to store the input from user and share it with fragments
    private var _textQR = MutableLiveData<String?>("")
    val textQR: LiveData<String?> = _textQR


    // a setter function that take input value, store it in ViewModel variable
    fun setTextQR(text: String?) {
        _textQR.value = text
    }



    // this function uses Coil to send request to QR generator api and form it into bitmap
    suspend fun generateQrCode( text: String?, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data("https://api.qrserver.com/v1/create-qr-code/?size=500x500&data=$text")
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_connection_error)
            .crossfade(true)
            .crossfade(500)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }




    fun getContentUri(imageView: ImageView, context: Context): Uri? {

        //take an imageview parameter turn into bitmap
        val bitmapDrawable = imageView.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        //create a folder to store image in permanently
        val imageFolder = File(context.cacheDir,"images")
        var contentUri: Uri? = null

        // try to catch when the image is not created
        try {
            imageFolder.mkdirs()
            val file = File(imageFolder, "shared_image.png")
            val stream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider" ,file)
        }catch (e: Exception) {
            showToast("${e.message}", context)
            Log.e("error", e.message.toString())
        }
        return contentUri
    }


    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}