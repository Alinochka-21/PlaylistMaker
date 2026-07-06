package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {

    fun getHistoryTrackList(): List<Track>

    fun saveHistoryTrackList(tracks: List<Track>)

    fun clearTrackHistory()
}