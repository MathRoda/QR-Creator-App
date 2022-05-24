package com.example.qrcreator.fragments.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHomeBinding
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val URL_REQUEST: String = "https://api.qrserver.com/v1/create-qr-code/?size=500x500&data="
private var SELECTED_TYPE: String = ""

@InternalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDatabaseViewModel: DatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mDatabaseViewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]
        setHasOptionsMenu(true)
        mDatabaseViewModel.setUrlQR(URL_REQUEST)
        SELECTED_TYPE = binding.linkText.tag.toString()

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

            val qrGeneratedText = binding.plainText.text.toString()
            mDatabaseViewModel.setTextQR(qrGeneratedText)
            onGenerateClicked()


           if (binding.plainText.text.isNotEmpty()){
               item.isVisible = false
           }
        }
        return true
    }



    // this function triggered when the generate button is clicked
    private fun onGenerateClicked() {
        lifecycleScope.launch {
            if(binding.plainText.text.isEmpty())  {
                showSnackBar("Please fill the text box !")
            }else {
                insertDataToDatabase()
                applyAnimations()
                navigateToSuccess()
            }
        }
    }

    // this function triggered to create a transition animation between fragments
    private suspend fun applyAnimations() {
        binding.icon.animate().alpha(0f).duration = 400L
        binding.titleTextview.animate().alpha(0f).duration = 400L
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

    private fun instagramRequest(){
        binding.instagram.setOnClickListener {
            binding.plainText.hint = "Please fill in your @  "
            binding.plainText.text.clear()
            SELECTED_TYPE = binding.instagram.tag.toString()
            mDatabaseViewModel.setUrlQR(URL_REQUEST + "instagram://user?username=")
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun linkAndTextRequest() {
        binding.linkText.setOnClickListener {
            binding.plainText.setHint(R.string.link_text_paste)
            SELECTED_TYPE = binding.linkText.tag.toString()
            mDatabaseViewModel.setUrlQR(URL_REQUEST)
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun facebookRequest() {
        binding.facebook.setOnClickListener {
            binding.plainText.hint= "Please fill in the Facebook ID"
            binding.plainText.text.clear()
            SELECTED_TYPE = binding.facebook.tag.toString()
            mDatabaseViewModel.setUrlQR(URL_REQUEST+ "fb://profile/")
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }



    private fun insertDataToDatabase() {
        val text = binding.plainText.text.toString()
        val insertType = SELECTED_TYPE

        if (inputCheck(text)) {
            lifecycleScope.launch {
                val history = History(text, insertType,  generateQrCode())
                mDatabaseViewModel.addQrHistory(history)
            }
        }

    }

    private suspend fun generateQrCode( ): Bitmap {

        val urlValue = mDatabaseViewModel.urlRequest.value
        val inputTextValue = mDatabaseViewModel.textQR.value

        // sending api request through coil to generate a QR bitmap
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data( urlValue + inputTextValue)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_connection_error)
            .crossfade(true)
            .crossfade(500)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }


    private fun inputCheck(text: String): Boolean {
        return !(TextUtils.isEmpty(text))
    }

    // this function triggered when navigate to next fragment
    private fun navigateToSuccess() {
        //val direction = HomeFragmentDirections.actionHomeFragmentToSuccessFragment()
        findNavController().navigate(R.id.action_homeFragment_to_successFragment)
    }
}