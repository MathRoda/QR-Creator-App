package com.example.qrcreator.ui

import android.annotation.SuppressLint
import android.nfc.Tag
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val URL_REQUEST = "https://api.qrserver.com/v1/create-qr-code/?size=500x500&data="

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: QrViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        viewModel.setUrlQR(URL_REQUEST)

        binding.generate.setOnClickListener {

            val qrGeneratedText = binding.plainText.text.toString()
            viewModel.setTextQR(qrGeneratedText)
            onGenerateClicked()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        instagramRequest()
        facebookRequest()
        linkAndTextRequest()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.home_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home_menu) {
            binding.plainText.text.clear()
            showSnackBar("Cleared !")
        }
        return true
    }



    // this function triggered when the generate button is clicked
    private fun onGenerateClicked() {
        lifecycleScope.launch {
            if(binding.plainText.text.isEmpty())  {
                showSnackBar("Please fill the text box !")
            }else {
                applyAnimations()
                navigateToSuccess()

            }
        }
    }

    // this function triggered to create a transition animation between fragments
    private suspend fun applyAnimations() {
        binding.generate.isClickable = false
        binding.icon.animate().alpha(0f).duration = 400L
        binding.titleTextview.animate().alpha(0f).duration = 400L
        binding.generate.animate().alpha(0f).duration = 400L
        binding.plainText.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
        binding.linkText.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
        binding.facebook.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
        binding.instagram.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L


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

    // to show different messages depends on the situation
    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay"){}
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        snackBar.show()
    }

    private fun instagramRequest() {
        binding.instagram.setOnClickListener {
            binding.plainText.hint = "Please fill in your @  "
            viewModel.setUrlQR(URL_REQUEST + "instagram://user?username=")
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun linkAndTextRequest() {
        binding.linkText.setOnClickListener {
            binding.plainText.setHint(R.string.link_text_paste)
            viewModel.setUrlQR(URL_REQUEST)
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun facebookRequest() {
        binding.facebook.setOnClickListener {
            binding.plainText.hint= "Please fill in the Facebook ID"
            viewModel.setUrlQR(URL_REQUEST+ "fb://profile/")
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }




    // this function triggered when navigate to next fragment
    private fun navigateToSuccess() {
        val direction = HomeFragmentDirections.actionHomeFragmentToSuccessFragment()
        findNavController().navigate(direction)
    }
}