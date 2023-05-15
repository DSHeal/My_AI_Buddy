package com.dsheal.youraimentor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsheal.youraimentor.R
import com.dsheal.youraimentor.databinding.ItemNavDrawerTopicsListBinding

class NavDrawerRecyclerAdapter(private val items: List<String>) :
    RecyclerView.Adapter<NavDrawerRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val binding = ItemNavDrawerTopicsListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(private val binding: ItemNavDrawerTopicsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.navItemText.text = item
        }
    }
}
