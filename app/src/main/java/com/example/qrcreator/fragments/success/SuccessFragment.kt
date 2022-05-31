package com.example.qrcreator.fragments.success

import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.qrcreator.databinding.FragmentSuccessBinding
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@InternalCoroutinesApi
class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val args:SuccessFragmentArgs by navArgs()
    private val savePath: String = Environment.getExternalStorageDirectory().path + "/QRCode/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentSuccessBinding.inflate(inflater, container, false)

        insertDataToDatabase()

        val bitmap = generateQr(args.qrText)
        binding.imageView.setImageBitmap(bitmap)
        val qrImage = binding.imageView

        binding.shareQR.setOnClickListener {
            shareQr(qrImage, requireContext())
            //saveQr(savePath, userInput, bitmap )

        }

        return binding.root
    }



    private fun insertDataToDatabase() {
        val text = args.qrTextDatabase
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


}