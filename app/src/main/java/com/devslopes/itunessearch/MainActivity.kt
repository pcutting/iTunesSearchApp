package com.devslopes.itunessearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.recyclerview.widget.LinearLayoutManager
import com.devslopes.itunessearch.databinding.ActivityMainBinding
import com.devslopes.itunessearch.databinding.FragmentStoreItemsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_store_items.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var searchResults : StoreItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.resultsFragmentContainer.id, StoreItemFragment())
            .commit()



        search.setOnSearchClickListener {
            fetchMatchingItems(binding, storeItemAdapter)
        }

        filter.setOnCheckedChangeListener { _, _ ->
            fetchMatchingItems(binding, storeItemAdapter)
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
            queryMap["limit"]="301"


        }
    }
}
