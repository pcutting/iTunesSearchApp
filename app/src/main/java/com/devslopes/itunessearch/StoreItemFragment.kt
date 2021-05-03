package com.devslopes.itunessearch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devslopes.itunessearch.databinding.FragmentStoreItemsBinding
import kotlinx.android.synthetic.main.fragment_store_items.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL


private const val TAG = "StoreItemFragment"

class StoreItemFragment : Fragment(R.layout.fragment_store_items) {

    private lateinit var binding: FragmentStoreItemsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStoreItemsBinding.bind(view)

        val storeItemAdapter = StoreItemAdapter()

        binding.apply {
            results.apply {
                adapter = storeItemAdapter
                layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,
                    false
                    )
            }

        }


    }


    fun fetchItems(query: Map<String,String>, onComplete: (MutableList<StoreItem>) -> Unit) {
        val permittedQueryKeys = listOf(
            "term", "country", "media", "entity", "attribute",
            "callback", "limit", "lang", "version", "explicit"
        )

        val validQuery = mutableMapOf<String, String>()
        query.forEach { (key, value) ->
            if (!permittedQueryKeys.contains(key) && value != "") {
                Log.e(TAG, "Invalid search parameter passed to search query: $key")
            } else {
                validQuery[key] = value
            }
        }

        val base = "https://itunes.apple.com/search?"
        var url = URL(base)
        url = url.buildUrl(validQuery)
        var data: String?
        CoroutineScope(Dispatchers.IO).launch {
            data = getItunesJson(url)
            try {
                val json = JSONObject(data ?: "")
                val store = StoreItems(json)
                onComplete(store.results)
            } catch (ex: java.lang.Exception) {
                Log.e(TAG, "unable to perform search, check your search criteria: Error $ex")
            }
        }
    }

}