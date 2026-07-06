package com.example.playlistmaker.search.data.local_data_source

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.data.clients.LocalDataBaseClient
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class SharedPrefLocalBaseClient(private val sharedPref: SharedPreferences) : LocalDataBaseClient {
    private val gson = Gson()

    override fun getHistoryTrackList(): List<Track> {
        val json = sharedPref.getString(KEY_TRACK_HISTORY, "")
        if (json.isNullOrEmpty()) return listOf()

        val tracksArray = gson.fromJson(json, Array<Track>::class.java)
        return tracksArray.toList()
    }

    override fun saveHistoryTrackList(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        sharedPref.edit {
            putString(KEY_TRACK_HISTORY, json)
        }
    }

    override fun clearTrackHistory() {
        sharedPref.edit { remove(KEY_TRACK_HISTORY) }
    }
    companion object {
        private const val KEY_TRACK_HISTORY = "search_history"
    }
}