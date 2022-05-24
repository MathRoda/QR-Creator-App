package com.example.qrcreator.fragments.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcreator.R
import com.example.qrcreator.databinding.FragmentHistoryBinding
import com.example.qrcreator.model.History
import com.example.qrcreator.viewmodels.DatabaseViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var mDatabaseViewModel: DatabaseViewModel
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

        mDatabaseViewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]
        mDatabaseViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        return binding.root
    }
}