package com.example.playlistmaker.search.domain.api_local

import com.example.playlistmaker.search.domain.models.Track

interface TrackHistoryInteractor {

    fun getTrackList(): List<Track>

    fun saveTrackList(tracks: List<Track>)

    fun addTrack(track: Track)

    fun clearTrackHistory()
}