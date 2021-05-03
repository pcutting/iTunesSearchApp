package com.devslopes.itunessearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val TAG = "StoreItemFragment"

class StoreItemFragment {

    fun fetchItems(query: Map<String,String>, onComplete: (MutableList<StoreItem>?) -> Unit) {
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
                Log.i(TAG, "Result count: ${store.count}")
                onComplete(store.results)
            } catch (ex: Exception) {
                Log.e(TAG, "unable to perform search, check your search criteria: Error $ex")
            }
        }
    }

    fun getImageData(url: String,  onComplete: (Bitmap) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(url)
                val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                onComplete(image)
            } catch (ex: Exception) {
                Log.e(TAG, "getImageData had an error: $ex")
            }
        }
    }
}