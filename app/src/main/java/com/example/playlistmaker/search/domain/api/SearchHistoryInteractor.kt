package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {

    suspend fun getTrackList(): List<Track>
    fun saveTrackList(tracks: List<Track>)

    suspend fun addTrack(track: Track)

    fun clearTrackHistory()
}