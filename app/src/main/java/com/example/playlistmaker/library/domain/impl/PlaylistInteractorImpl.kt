package com.example.playlistmaker.library.domain.impl

import android.net.Uri
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(
        id: Long,
        list: List<Long>
    ) {
        repository.deletePlaylist(id,list)
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

    override suspend fun getPlaylistInfo(id: Long): Playlist {
        return repository.getPlaylistInfo(id)
    }

    override fun getTracksFromPlaylist(listId: ArrayList<Long>): Flow<List<Track>> {
        return repository.getTracksFromPlaylist(listId)
    }

    override suspend fun deleteTrackFromPlaylist(idPlaylist: Long, idTrack: Long) {
        repository.deleteTrackFromPlaylist(idPlaylist,idTrack)
    }

    override suspend fun changePlaylist(
        id: Long,
        newName: String,
        newCover: Uri?,
        newDescription: String
    ) {
        repository.changePlaylist(id, newName, newCover, newDescription)
    }
}