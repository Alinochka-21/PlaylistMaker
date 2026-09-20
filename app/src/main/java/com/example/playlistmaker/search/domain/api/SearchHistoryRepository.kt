package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun getHistoryTrackList(): List<Track>

    fun saveHistoryTrackList(tracks: List<Track>)

    fun clearTrackHistory()
}