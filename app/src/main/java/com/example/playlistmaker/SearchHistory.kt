package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import androidx.core.content.edit

class SearchHistory(private val sharedPref: SharedPreferences){

    companion object {
        private const val KEY_TRACK_HISTORY = "search_history"
        private const val MAX_SIZE = 10
    }

    private val gson = Gson()

    fun getHistoryTrackList(): ArrayList<Track>{
        val json = sharedPref.getString(KEY_TRACK_HISTORY, "")
        if (json.isNullOrEmpty()) return arrayListOf()

        val tracksArray = gson.fromJson(json, Array<Track>::class.java)
        return tracksArray.toCollection(ArrayList())
    }

    fun saveHistoryTrackList(tracks: ArrayList<Track>){
        val json = gson.toJson(tracks.toTypedArray())
        sharedPref.edit {
            putString(KEY_TRACK_HISTORY, json)
        }
    }

    fun addTrack(track: Track){
        val trackHistory = getHistoryTrackList()

        trackHistory.removeAll{it.trackId == track.trackId}
        trackHistory.add(0, track)

        if (trackHistory.size > MAX_SIZE){
            trackHistory.removeAt(trackHistory.size-1)
        }

        saveHistoryTrackList(trackHistory)
    }

    fun clearTrackHistory(){
        sharedPref.edit { remove(KEY_TRACK_HISTORY) }
    }
}