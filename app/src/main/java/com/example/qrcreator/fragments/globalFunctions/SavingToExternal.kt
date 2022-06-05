package com.example.qrcreator.fragments.globalFunctions

import android.Manifest
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaCodec.MetricsConstants.MODE
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qrcreator.fragments.globalFunctions.sdk29AndUp
import com.example.qrcreator.fragments.success.SuccessFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.IOException

@InternalCoroutinesApi
fun Fragment.updateOrRequestPermissions(
    _readPermissionGranted: Boolean,
    _writePermissionGranted: Boolean,
    permissionLauncher: ActivityResultLauncher<Array<String>>) {

    var readPermissionGranted = _readPermissionGranted
    var writePermissionGranted = _writePermissionGranted

    val hasReadPermissions = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    val hasWritePermissions = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    readPermissionGranted = hasReadPermissions
    writePermissionGranted = hasWritePermissions || minSdk29

    val permissionToRequest = mutableListOf<String>()
    if (!writePermissionGranted) {
        permissionToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (!readPermissionGranted) {
        permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    if(permissionToRequest.isNotEmpty()) {
        permissionLauncher.launch(permissionToRequest.toTypedArray())
    }
}

@InternalCoroutinesApi
suspend fun Fragment.saveQRtoStorage(displayName: String, bmp: Bitmap): Boolean {

    withContext(Dispatchers.IO) {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            sdk29AndUp {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/QR Creator")
            }
            put(MediaStore.Images.Media.MIME_TYPE, "Image/Jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return@withContext try {
            requireContext().contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                requireContext().contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Couldn't Save Bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create Media store entry")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    return false
}

