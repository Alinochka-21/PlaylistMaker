package com.example.playlistmaker.library.data.repository_impl

import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.db.AppDataBase
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val converter: PlaylistDbConverter,
    private val dataBase: AppDataBase
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistEntity = converter.toPlaylistEntity(playlist)
        dataBase.getPlaylistDao().addPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        dataBase.getPlaylistDao()
            .getPlaylists()
            .map { entities ->
                convertToPlaylists(entities)
            }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistEntity = converter.toPlaylistEntity(playlist)
        dataBase.getPlaylistDao().deletePlaylist(playlistEntity)
    }

    override suspend fun addTrack(
        idPlaylistEntity: Long,
        track: Track
    ) {
        val playlistTrackEntity = mapToPlaylistTrackEntity(track)
        dataBase.getPlaylistTracksDao().addTrack(playlistTrackEntity)

        val oldIdString = dataBase.getPlaylistDao().getTracksId(idPlaylistEntity)
        val oldIdList = converter.stringToList(oldIdString)
        val newIdList =  oldIdList.apply { add(track.trackId) }
        val newIdString = converter.listToString(newIdList)
        dataBase.getPlaylistDao().addTrack(idPlaylistEntity, newIdString)
    }

    override suspend fun addSuccessCheck(playlistId: Long, trackId: Long): Boolean {
        val stringList = dataBase.getPlaylistDao().getTracksId(playlistId)
        val idList = converter.stringToList(stringList)
        return idList.contains(trackId)
    }
    private fun convertToPlaylists(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlist -> converter.toPlaylist(playlist) }
    }

    private fun mapToPlaylistTrackEntity(track: Track): PlaylistTracksEntity {
        return PlaylistTracksEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            addedAt = System.currentTimeMillis()
        )
    }
}