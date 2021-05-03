package com.devslopes.itunessearch

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devslopes.itunessearch.databinding.ItemStoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

private const val TAG = "StoreItemAdapter.kt"
class StoreItemAdapter : ListAdapter<StoreItem, StoreItemAdapter.StoreItemViewHolder>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<StoreItem>() {
            override fun areItemsTheSame(oldItem: StoreItem, newItem: StoreItem): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: StoreItem, newItem: StoreItem): Boolean {
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

        fun onBind(item: StoreItem) {
            binding.title.text = item.trackName
            binding.detailTextLabel.text = item.artistName


            StoreItemFragment().getImageData(
                item.artworkUrl100
            ) {
                //Set image with data somehow.
                //val imageBytes = it.toByteArray()
                val image = BitmapFactory.decodeByteArray(
                    it,
                    0,
                    it.size)
                binding.imageView.setImageBitmap(image)
                Log.i(TAG, "ImageData. ${it.size}")
            }
        }
    }
}