package com.example.qrcreator.fragments.home
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
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

        linkAndTextRequest()
        binding.clear.setOnClickListener {
            clearText()
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
        binding.clear.animate()
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
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))


            binding.instagram.isActivated = true
            binding.linkText.isActivated = false
            binding.facebook.isActivated = false


        }
    }

    private fun linkAndTextRequest() {
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

    private fun facebookRequest(){
        binding.facebook.setOnClickListener {
            binding.plainText.hint= "Please fill in the Facebook ID"
            binding.facebook.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.teal_200))
            binding.linkText.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            binding.instagram.drawable.setTint(ContextCompat.getColor(requireContext(), R.color.white))

            binding.facebook.isActivated = true
            binding.instagram.isActivated = false
            binding.linkText.isActivated = false

        }
    }




    // this function triggered when navigate to next fragment
    private fun navigateToSuccess() {
        var qrText = binding.plainText.text.toString()
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

       val direction = HomeFragmentDirections.actionHomeFragmentToSuccessFragment(qrText, qrType)
        findNavController().navigate(direction)
    }


    private fun clearText() {
        if (binding.plainText.text.isNotEmpty()) {
        binding.plainText.text.clear()
        showSnackBar("Cleared!")
        }
    }
}