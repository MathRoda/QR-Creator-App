package com.example.qrcreator.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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

            val qrImage = binding.imageView
            val qrText = viewModel.textQR.value

            viewModel.generateQrCode(qrImage, qrText)

            delay(600)
            binding.textView.setText(R.string.your_qr_code_has_been_generated)
        }

        binding.saveQR.setOnClickListener {

        }

        return binding.root
    }




}