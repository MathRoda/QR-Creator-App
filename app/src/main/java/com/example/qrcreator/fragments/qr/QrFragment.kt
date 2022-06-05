package com.example.qrcreator.fragments.qr

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentQrBinding
import com.example.qrcreator.fragments.globalFunctions.generateQr
import com.example.qrcreator.fragments.globalFunctions.shareQr
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class QrFragment : Fragment() {

    private lateinit var binding: FragmentQrBinding
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()
    private val args by navArgs<QrFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentQrBinding.inflate(inflater, container, false)

        binding.type.text = args.currentQR.type
        textFigureOut(binding, args)

        lifecycleScope.launch {
            val bmp =  generateQr(args.currentQR.qrText)
            binding.imageView.setImageBitmap(bmp)

            binding.shareQR.setOnClickListener {
                shareQr(bmp, requireContext())
            }
        }

        setHasOptionsMenu(true)
        return  binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            deleteQrHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteQrHistory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mDatabaseViewModel.deleteQrHistory(args.currentQR)
            Toast.makeText(requireContext(), "Removed ${args.currentQR.text}", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle("Delete ${args.currentQR.text} ?")
        builder.setMessage("Are You Sure You Want to Delete This QR ?")
        builder.create().show()
    }

}