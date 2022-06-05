package com.example.qrcreator.fragments.globalFunctions

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
fun Fragment.generateQr(userInput: String): Bitmap {
    val qrEncoder = QRGEncoder(userInput, null, QRGContents.Type.TEXT, 700)
    return qrEncoder.bitmap
}

@InternalCoroutinesApi
fun Fragment.showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}