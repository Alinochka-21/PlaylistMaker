package com.example.playlistmaker


data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String?,
    val releaseDate: String?)