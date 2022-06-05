package com.example.qrcreator.fragments.success

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qrcreator.databinding.FragmentSuccessBinding
import com.example.qrcreator.fragments.globalFunctions.*
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@InternalCoroutinesApi
class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val args:SuccessFragmentArgs by navArgs()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var readPermissionGranted = false
    private var writePermissionGranted = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=  FragmentSuccessBinding.inflate(inflater, container, false)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
            writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
        }
        updateOrRequestPermissions(readPermissionGranted, writePermissionGranted, permissionLauncher)


        val bitmap = generateQr(args.qrText)
        binding.imageView.setImageBitmap(bitmap)
        insertDataToDatabase(bitmap)

        binding.shareQR.setOnClickListener {
            shareQr(bitmap, requireContext())
        }

        binding.saveQR.setOnClickListener {
            lifecycleScope.launch {
               if (!writePermissionGranted) {
                    saveQRtoStorage(UUID.randomUUID().toString(), bitmap)
                    showToast("Photo saved successfully", requireContext())
                } else {
                    showToast("Failed to save photo", requireContext())
                }
            }
        }

        return binding.root
    }





    private fun insertDataToDatabase(bmp: Bitmap) {
        val text = args.text
        val qrText = args.qrText
        val insertType = args.qrType

        if (inputCheck(text)) {
            lifecycleScope.launch {
                val history = History(text, qrText,  insertType, bmp)
                mDatabaseViewModel.addQrHistory(history)
            }
        }

    }


    private fun inputCheck(text: String): Boolean {
        return !(TextUtils.isEmpty(text))
    }




}