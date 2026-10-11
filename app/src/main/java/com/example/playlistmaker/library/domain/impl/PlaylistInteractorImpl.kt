package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getPlaylist(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addTrack(
        idPlaylist: Long,
        track: Track
    ) {
        repository.addTrack(idPlaylist, track)
    }

    override suspend fun addSuccessCheck(
        playlistId: Long,
        trackId: Long
    ): Boolean {
        return repository.addSuccessCheck(playlistId,trackId)
    }
}