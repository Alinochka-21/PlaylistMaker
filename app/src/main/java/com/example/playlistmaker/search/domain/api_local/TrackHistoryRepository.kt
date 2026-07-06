package com.example.playlistmaker.search.domain.api_local

import com.example.playlistmaker.search.domain.models.Track

interface TrackHistoryRepository {

    fun getHistoryTrackList(): List<Track>

    fun saveHistoryTrackList(tracks: List<Track>)

    fun clearTrackHistory()
}