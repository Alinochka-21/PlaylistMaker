package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SelectedTrackInteractor {
    suspend fun addSelectedTrack(track: Track)
    suspend fun deleteSelectedTrack(track: Track)
    fun getSelectedList(): Flow<List<Track>>
}