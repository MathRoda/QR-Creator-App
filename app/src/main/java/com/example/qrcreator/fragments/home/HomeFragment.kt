package com.example.qrcreator.fragments.home
import android.os.Bundle
import android.view.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        linkAndTextRequest(binding)
            binding.clear.setOnClickListener {
                clearText(binding)
            }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        instagramRequest(binding)
        facebookRequest(binding)
        linkAndTextRequest(binding)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.home_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home_menu) {

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
                showSnackBar(binding, "Please fill the text box !")
            }else {
                applyAnimations(binding)
                navigateToSuccess()

            }
        }
    }

    // this function triggered when navigate to next fragment
    private fun navigateToSuccess() {
        var qrText = binding.plainText.text.toString()
        val text = qrText
        val qrType: String

        when {
            binding.instagram.isActivated -> {
                qrText = "instagram://user?username=$qrText"
                qrType = binding.instagram.tag.toString()
            }
            binding.facebook.isActivated -> {
                qrText = "fb://profile/$qrText"
                qrType = binding.facebook.tag.toString()
            }
            else -> {
                qrText = binding.plainText.text.toString()
                qrType = binding.linkText.tag.toString()
            }
        }

        val direction = HomeFragmentDirections.actionHomeFragmentToSuccessFragment(text, qrType, qrText)
        findNavController().navigate(direction)
    }



}