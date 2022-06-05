package com.example.qrcreator.fragments.globalFunctions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File
import java.io.FileOutputStream

/**
*
*/
@InternalCoroutinesApi
fun Fragment.shareQr(bitmap: Bitmap, context: Context) {
val contentUri = getContentUri(bitmap, context)
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
fun Fragment.getContentUri(bitmap: Bitmap, context: Context): Uri? {

//take an imageview parameter turn into bitmap
//val bitmapDrawable = imageView.drawable as BitmapDrawable
//val bitmap = bitmapDrawable.bitmap

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

