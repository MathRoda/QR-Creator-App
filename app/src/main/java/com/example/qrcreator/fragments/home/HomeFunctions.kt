package com.example.qrcreator.fragments.home

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

fun HomeFragment.instagramRequest(binding: FragmentHomeBinding) {
    binding.instagram.setOnClickListener {
        binding.plainText.hint = "Please fill in your @  "
        binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
        binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))


        binding.instagram.isActivated = true
        binding.linkText.isActivated = false
        binding.facebook.isActivated = false


    }
}

 fun HomeFragment.linkAndTextRequest(binding: FragmentHomeBinding) {
    binding.linkText.setOnClickListener {
        binding.plainText.setHint(R.string.link_text_paste)
        binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
        binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        binding.linkText.isActivated = true
        binding.instagram.isActivated = false
        binding.facebook.isActivated = false


    }
}

 fun HomeFragment.facebookRequest(binding: FragmentHomeBinding) {
     binding.facebook.setOnClickListener {
         binding.plainText.hint = "Please fill in the Facebook ID"
         binding.facebook.drawable.setTint(
             ContextCompat.getColor(
                 requireContext(),
                 R.color.teal_200
             )
         )
         binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
         binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))

         binding.facebook.isActivated = true
         binding.instagram.isActivated = false
         binding.linkText.isActivated = false

     }
 }





// to show different messages depends on the situation
      fun HomeFragment.showSnackBar(binding: FragmentHomeBinding ,message: String) {
         val snackBar = Snackbar.make(
             binding.rootLayout,
             message,
             Snackbar.LENGTH_SHORT
         )
         snackBar.setAction("Okay"){}
         snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
    snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
         snackBar.show()
     }
     


      fun HomeFragment.clearText(binding: FragmentHomeBinding) {
         if (binding.plainText.text.isNotEmpty()) {
             binding.plainText.text.clear()
             showSnackBar(binding, "Cleared!")
         }
     }