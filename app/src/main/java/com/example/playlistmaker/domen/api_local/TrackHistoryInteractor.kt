package com.example.playlistmaker.domen.api_local

import com.example.playlistmaker.domen.models.Track

interface TrackHistoryInteractor {

    fun getTrackList(): List<Track>

    fun saveTrackList(tracks: List<Track>)

    fun addTrack(track: Track)

    fun clearTrackHistory()
}