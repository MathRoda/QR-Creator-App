package com.example.qrcreator.fragments.home

import androidx.fragment.app.Fragment
import com.example.qrcreator.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay

// this function triggered to create a transition animation between fragments
 suspend fun HomeFragment.applyAnimations(binding:FragmentHomeBinding) {
    val plaintext = binding.plainText
    val clear = binding.clear
    val linkText = binding.linkText
    val facebook = binding.facebook
    val instagram = binding.instagram

    binding.icon.animate().alpha(0f).duration = 400L
    binding.titleTextview.animate().alpha(0f).duration = 400L


    (arrayOf(plaintext, clear, linkText, facebook, instagram)).forEach { view ->
        view.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
    }

    delay(300)
    binding.SuccessBg.animate().alpha(1f).duration = 600L
    binding.SuccessBg.animate().rotationBy(720f).duration = 600L
    binding.SuccessBg.animate().scaleXBy(900f).duration = 800L
    binding.SuccessBg.animate().scaleYBy(900f).duration = 800L

    delay(500)
    binding.successImageView.animate().alpha(1f)
        .duration = 1000L

    delay(1500L)


}