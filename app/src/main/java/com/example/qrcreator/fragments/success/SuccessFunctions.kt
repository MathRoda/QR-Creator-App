package com.example.qrcreator.fragments.success

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.FileProvider
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File
import java.io.FileOutputStream

    // this function start new activity share launcher to share QR code
    @InternalCoroutinesApi
    fun SuccessFragment.shareQr(imageView: ImageView, context: Context) {
        val contentUri = getContentUri(imageView, context)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
        }

        startActivity(Intent.createChooser(intent, "Share via"))

    }


     @InternalCoroutinesApi
     fun SuccessFragment.getContentUri(imageView: ImageView, context: Context): Uri? {

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


     @InternalCoroutinesApi
     fun SuccessFragment.showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
     }

@InternalCoroutinesApi
fun SuccessFragment.generateQr(userInput: String): Bitmap {
    val qrEncoder = QRGEncoder(userInput, null, QRGContents.Type.TEXT, 700)
    return qrEncoder.bitmap
}

    /* private fun saveQr(savePath: String, userInput: String, bitmap: Bitmap) {

        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                try {
                    val qrSaver = QRGSaver()
                    qrSaver.save(savePath, userInput.trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(requireContext(),"not available", Toast.LENGTH_SHORT).show()
            }

        }

} */