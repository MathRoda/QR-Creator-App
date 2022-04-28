package com.example.qrcreator.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private val viewModel: QrViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentSuccessBinding.inflate(inflater, container, false)


        lifecycleScope.launch {

            val qrText = viewModel.textQR.value
            val bitmapQR = viewModel.generateQrCode( qrText, requireContext())
            binding.imageView.setImageBitmap(bitmapQR)

            delay(100)
            binding.textView.setText(R.string.your_qr_code_has_been_generated)
        }

        binding.shareQR.setOnClickListener {
            val imageView = binding.imageView
            shareImage(imageView, requireContext())
        }

        return binding.root
    }


    // this function start new activity share launcher to share QR code
    private fun shareImage(imageView: ImageView, context: Context) {
        val contentUri = viewModel.getContentUri(imageView, context)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
        }

        startActivity(Intent.createChooser(intent, "Share via"))

    }



}