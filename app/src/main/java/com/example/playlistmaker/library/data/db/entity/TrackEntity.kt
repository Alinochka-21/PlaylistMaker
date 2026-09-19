package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity (
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
    val previewUrl: String )