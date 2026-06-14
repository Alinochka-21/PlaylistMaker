package com.example.playlistmaker.data.local_data_source
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.data.LocalDataBaseClient
import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.Gson
import kotlin.collections.removeAll

class SharedPrefLocalBaseClient(private val sharedPref: SharedPreferences) : LocalDataBaseClient {
    private val gson = Gson()

    override fun getHistoryTrackDtoList(): List<TrackDto> {
        val json = sharedPref.getString(KEY_TRACK_DTO_HISTORY, "")
        if (json.isNullOrEmpty()) return listOf()

        val tracksArray = gson.fromJson(json, Array<TrackDto>::class.java)
        return tracksArray.toList()
    }

    override fun saveHistoryTrackDtoList(tracksDto: List<TrackDto>) {
        val json = gson.toJson(tracksDto)
        sharedPref.edit {
            putString(KEY_TRACK_DTO_HISTORY, json)
        }
    }

    override fun addTrackDto(trackDto: TrackDto) {
        val trackHistory = getHistoryTrackDtoList().toMutableList()

        trackHistory.apply{
            removeAll{it.trackId == trackDto.trackId}
            add(0, trackDto)
        }

        if (trackHistory.size > MAX_TRACK_COUNT){
            trackHistory.removeAt(trackHistory.size-1)
        }

        saveHistoryTrackDtoList(trackHistory)
    }

    override fun clearTrackDtoHistory() {
        sharedPref.edit { remove(KEY_TRACK_DTO_HISTORY) }
    }
    companion object {
        private const val KEY_TRACK_DTO_HISTORY = "search_history"
        private const val MAX_TRACK_COUNT = 10
    }
}