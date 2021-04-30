package com.devslopes.itunessearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devslopes.itunessearch.databinding.ItemStoreBinding

class StoreItemAdapter : ListAdapter<String, StoreItemAdapter.StoreItemViewHolder>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoreBinding.inflate(inflater, parent, false)
        return StoreItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class StoreItemViewHolder(
        private val binding: ItemStoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(title: String) {
            binding.title.text = title
        }
    }
}