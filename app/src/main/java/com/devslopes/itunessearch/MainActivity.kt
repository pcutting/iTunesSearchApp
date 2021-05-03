package com.devslopes.itunessearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.devslopes.itunessearch.databinding.ActivityMainBinding


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storeItemAdapter = StoreItemAdapter()
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
        val queryMap = mutableMapOf<String,String>()
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
}
