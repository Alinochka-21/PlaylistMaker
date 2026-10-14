package com.example.playlistmaker.library.domain.db

import android.net.Uri
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Long, list: List<Long>)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrack(idPlaylistEntity: Long, track: Track)

    suspend fun addSuccessCheck(playlistId: Long, trackId: Long): Boolean

    suspend fun getPlaylistInfo(id: Long): Playlist

    fun getTracksFromPlaylist(listId: ArrayList<Long>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist( idPlaylist: Long, idTrack: Long)

    suspend fun changePlaylist(id: Long, newName: String, newCover: Uri?, newDescription: String)
}