package com.example.playlistmaker.domen.api_local

import com.example.playlistmaker.domen.models.Track

interface TrackHistoryRepository {

    fun getHistoryTrackList(): List<Track>

    fun saveHistoryTrackList(tracks: List<Track>)

    fun clearTrackHistory()
}