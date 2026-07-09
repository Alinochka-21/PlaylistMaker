package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.clients.StorageClient
import com.google.gson.Gson
import java.lang.reflect.Type
import androidx.core.content.edit


class PrefsStorageClient<T>(
    private val sharedPref: SharedPreferences,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

    private val gson = Gson()

    override fun getData(): T? {
        val json = sharedPref.getString(dataKey, null)
        return if (json == null) {
            null
        } else {
            gson.fromJson(json, type)
        }
    }

    override fun saveData(data: T) {
        val json = gson.toJson(data, type)
        sharedPref.edit{ putString(dataKey, json)}
    }

    override fun clearData() {
        sharedPref.edit { remove(dataKey) }
    }

    companion object {
        private const val KEY_TRACK_HISTORY = "search_history"
    }
}