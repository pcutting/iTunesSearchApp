package com.devslopes.itunessearch

import android.net.http.HttpResponseCache
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.devslopes.itunessearch.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeItemAdapter = StoreItemAdapter()

        try {
            val httpCacheDir = File(this.getCacheDir(), "http")
            val httpCacheSize = (80 * 1024 * 1024).toLong() // 80 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
        } catch (e: IOException) {
            Log.i(TAG, "HTTP response cache installation failed:$e")
        }

        binding.apply {
            results.apply {
                adapter = storeItemAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }

            search.setOnSearchClickListener {
                fetchMatchingItems(binding, storeItemAdapter)
            }

            filter.setOnCheckedChangeListener { _, _ ->
                fetchMatchingItems(binding, storeItemAdapter)
            }
        }
    }

    private fun fetchMatchingItems(binding: ActivityMainBinding, storeItemAdapter: StoreItemAdapter) {
        val queryMap = mutableMapOf<String, String>()
        val searchTerm = binding.search.query.toString()
        val mediaType = when (binding.filter.checkedRadioButtonId) {
            R.id.movie -> "movie"
            R.id.music -> "music"
            R.id.software -> "software"
            R.id.eBook -> "eBook"
            else -> "all"
        }

        if (searchTerm.isNotEmpty()) {
            queryMap["term"]=searchTerm
            queryMap["media"]=mediaType
            queryMap["lang"]="en_us"
            queryMap["limit"]="11"

            StoreItemFragment().fetchItems(
                queryMap
            ) {
                storeItemAdapter.submitList(it)
                Log.i(TAG, "Searched items. $it.count()")
            }
            Log.i(TAG, "after storeItemFragment called... query: $queryMap")
        }
    }


    override fun onStop() {
        super.onStop()

        val cache = HttpResponseCache.getInstalled()
        cache?.flush()

    }

}
