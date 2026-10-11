package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.net.toUri

class PlaylistDbConverter (private val gson: Gson) {

    private val type = object : TypeToken<ArrayList<Long>>() {}.type
    fun toPlaylistEntity(playlist: Playlist): PlaylistEntity {

        val stringIdList = gson.toJson(playlist.trackIdList, type)
        val description = playlist.playlistDescription.ifBlank { null }

        return PlaylistEntity(
            playlistName = playlist.playlistName,
            playlistDescription = description,
            playlistCover = playlist.coverUri.toString(),
            playlistId = playlist.playlistId,
            tracklist = stringIdList,
            playlistSize = playlist.trackIdList.size
        )
    }

    fun toPlaylist(playlistEntity: PlaylistEntity) : Playlist {

        val trackIdList = gson.fromJson<ArrayList<Long>>(playlistEntity.tracklist,type)
        val uri = if (playlistEntity.playlistCover == "null") null else playlistEntity.playlistCover!!.toUri()

        return Playlist(
            playlistName = playlistEntity.playlistName,
            playlistDescription = playlistEntity.playlistDescription ?: "",
            coverUri = uri,
            trackIdList = trackIdList ?: arrayListOf(),
            playlistSize = playlistEntity.playlistSize,
            playlistId = playlistEntity.playlistId
        )
    }

    fun stringToList(string: String): ArrayList<Long> {
        return gson.fromJson<ArrayList<Long>>(string,type)
    }
    fun listToString(list:ArrayList<Long>): String {
        return gson.toJson(list,type)
    }
}