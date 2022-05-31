package com.example.qrcreator.fragments.success

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.qrcreator.databinding.FragmentSuccessBinding
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@InternalCoroutinesApi
class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private lateinit var mDatabaseViewModel: DatabaseViewModel
    private val args:SuccessFragmentArgs by navArgs()
    private val savePath: String = Environment.getExternalStorageDirectory().path + "/QRCode/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentSuccessBinding.inflate(inflater, container, false)

        val qrImage = binding.imageView
        mDatabaseViewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]

        val userInput = args.qrText
        val qrEncoder = QRGEncoder(userInput, null, QRGContents.Type.TEXT, 700)
        val bitmap = qrEncoder.bitmap
        qrImage.setImageBitmap(bitmap)

        insertDataToDatabase()


        binding.shareQR.setOnClickListener {
            shareQr(qrImage, requireContext())
            //saveQr(savePath, userInput, bitmap )

        }

        return binding.root
    }


    // this function start new activity share launcher to share QR code
    private fun shareQr(imageView: ImageView, context: Context) {
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


    private fun getContentUri(imageView: ImageView, context: Context): Uri? {

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


    private fun insertDataToDatabase() {
        val text = args.qrText
        val insertType = args.qrType

        if (inputCheck(text)) {
            lifecycleScope.launch {
                val history = History(text, insertType)
                mDatabaseViewModel.addQrHistory(history)
            }
        }

    }


    private fun inputCheck(text: String): Boolean {
        return !(TextUtils.isEmpty(text))
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}