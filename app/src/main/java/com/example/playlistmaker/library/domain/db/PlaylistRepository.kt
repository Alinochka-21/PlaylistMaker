package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun addTrack(idPlaylistEntity: Long, track: Track)

    suspend fun addSuccessCheck(playlistId: Long, trackId: Long): Boolean
}