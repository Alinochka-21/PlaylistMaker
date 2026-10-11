package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    var playlistName: String,
    var playlistDescription: String?,
    var playlistCover: String?,
    var tracklist: String?,
    var playlistSize: Int,
    val addedAt: Long = System.currentTimeMillis()
)