package com.example.qrcreator.fragments.success

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.qrcreator.databinding.FragmentSuccessBinding
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File
import java.io.FileOutputStream


@InternalCoroutinesApi
class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private lateinit var mDatabaseViewModel: DatabaseViewModel
    private lateinit var history: History

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentSuccessBinding.inflate(inflater, container, false)

       /* mDatabaseViewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]
        mDatabaseViewModel.getQrHistory(id).observe(viewLifecycleOwner, Observer {
            binding.imageView.load(it)
        })*/


       /* lifecycleScope.launch {

            val qrText = viewModel.textQR.value
            val bitmapQR = viewModel.generateQrCode( qrText, requireContext())
            binding.imageView.setImageBitmap(bitmapQR)

            delay(100)
            binding.textView.setText(R.string.your_qr_code_has_been_generated)
        }*/

       /* binding.shareQR.setOnClickListener {
            val imageView = binding.imageView
            shareImage(imageView, requireContext())
        }*/

        return binding.root
    }


    // this function start new activity share launcher to share QR code
    private fun shareImage(imageView: ImageView, context: Context) {
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

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}