package com.example.playlistmaker.search.data.clients

import com.example.playlistmaker.search.domain.models.Track

interface LocalDataBaseClient {

    fun getHistoryTrackList(): List<Track>

    fun saveHistoryTrackList(tracks: List<Track>)

    fun clearTrackHistory()
}