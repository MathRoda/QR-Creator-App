package com.example.qrcreator.fragments.history

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.qrcreator.databinding.CustomRowBinding
import com.example.qrcreator.model.History

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    private var historyList = emptyList<History>()

    class ViewHolder(binding: CustomRowBinding): RecyclerView.ViewHolder(binding.root) {
        private val qrBitmap: ImageView = binding.QrBitmap
        private val type = binding.type
        private val text = binding.text

        var rowLayout: ConstraintLayout = binding.rowLayout

        fun bind(history: History) {
            text.text = history.text
            type.text = history.type
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           CustomRowBinding
               .inflate
                   (LayoutInflater.from(parent.context)
                   , parent
                   , false)
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
       return historyList.size
    }

    fun setData(history: List<History>) {
        this.historyList = history
        notifyDataSetChanged()
    }
}