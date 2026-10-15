package com.example.playlistmaker.library.data.repository_impl

import android.net.Uri
import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.db.AppDataBase
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
        dataBase.getPlaylistDao().changeTrackList(idPlaylistEntity, newIdString)
    }

    override suspend fun addSuccessCheck(playlistId: Long, trackId: Long): Boolean {
        val stringList = dataBase.getPlaylistDao().getTracksId(playlistId)
        val idList = converter.stringToList(stringList)
        return idList.contains(trackId)
    }

    override suspend fun getPlaylistInfo(id: Long): Playlist {
        val entity = dataBase.getPlaylistDao().getPlaylistInfo(id)
        return converter.toPlaylist(entity)
    }

    override fun getTracksFromPlaylist(listId: ArrayList<Long>): Flow<List<Track>> =
        dataBase.getPlaylistTracksDao().getTracks(listId).map { entities ->
            val favoriteIds = dataBase.getTrackDao().getAllTrackIds().first()

            val sorted = listId.mapNotNull { id ->
                entities.find { it.trackId == id }
            }

            sorted.map { entity ->
                val track = converter.toTrack(entity)
                track.copy(isFavorite = track.trackId in favoriteIds)
            }
        }

    override suspend fun deleteTrackFromPlaylist(
        idPlaylist: Long,
        idTrack: Long
    ) {

        val allStringId = dataBase.getPlaylistDao().getAllTracksId()
        val allIds: MutableList<Long> = allStringId.flatMap { str ->
            converter.stringToList(str)
        }.toMutableList()

        val currentEntity = dataBase.getPlaylistDao().getPlaylistInfo(idPlaylist)
        val tracksIdCurrentPlaylist =  converter.stringToList(currentEntity.tracklist!!)

        allIds.remove(idTrack)
        if (!allIds.contains(idTrack)) {
            dataBase.getPlaylistTracksDao().deleteTrack(idTrack)
        }

        tracksIdCurrentPlaylist.remove(idTrack)
        val newStringId = converter.listToString(tracksIdCurrentPlaylist)
        dataBase.getPlaylistDao().changeTrackList(currentEntity.playlistId, newStringId)
    }

    override suspend fun changePlaylist(
        id: Long,
        newName: String,
        newCover: Uri?,
        newDescription: String
    ) {
        dataBase.getPlaylistDao().changePlaylist(
            id = id,
            newName = newName,
            newCover = newCover.toString(),
            newDescription = newDescription
        )
    }

    override suspend fun deletePlaylist(id: Long, list: List<Long>) {
        dataBase.getPlaylistDao().deletePlaylist(id)

        val allStringId = dataBase.getPlaylistDao().getAllTracksId()

        val allIds: List<Long> = allStringId.flatMap { str ->
            converter.stringToList(str)
        }

        list.forEach { trackId ->
            if (!allIds.contains(trackId)) {
                dataBase.getPlaylistTracksDao().deleteTrack(trackId)
            }
        }
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
            addedAt = System.currentTimeMillis(),
            isFavorite = track.isFavorite
        )
    }
}