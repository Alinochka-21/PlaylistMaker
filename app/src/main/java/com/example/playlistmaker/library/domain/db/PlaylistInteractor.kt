package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylist(): Flow<List<Playlist>>

    suspend fun addTrack(idPlaylist: Long, track: Track)

    suspend fun addSuccessCheck(playlistId: Long, trackId: Long): Boolean
}