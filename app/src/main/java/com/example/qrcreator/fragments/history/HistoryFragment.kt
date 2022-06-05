package com.example.qrcreator.fragments.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHistoryBinding
import com.example.qrcreator.fragments.globalFunctions.showToast
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val mDatabaseViewModel: DatabaseViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater , container, false)

        val adapter = HistoryAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        mDatabaseViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val recyclerViewEmpty = binding.recyclerView.isEmpty()
        if (item.itemId == R.id.delete_menu) {
            if (recyclerViewEmpty) {
                showToast("Create QR First!", requireContext())
            } else {
                deleteAllHistory()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllHistory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mDatabaseViewModel.deleteAllHistory()
            Toast.makeText(requireContext(), "All History Removed", Toast.LENGTH_SHORT).show()

        }

        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle(" Are You Sure ?")
        builder.setMessage(" You Want to Delete All History ")
        builder.create().show()
    }




}