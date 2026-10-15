package com.example.playlistmaker.library.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "playlist_tracks_table")
data class PlaylistTracksEntity (
    @PrimaryKey(autoGenerate = false)
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String?,
    val releaseDate: String?,
    val previewUrl: String,
    @ColumnInfo(defaultValue = "0")
    var isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis() )
