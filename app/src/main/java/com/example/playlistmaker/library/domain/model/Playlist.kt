package com.example.playlistmaker.library.domain.model

import android.net.Uri

data class Playlist (
    var playlistId: Long,
    var playlistName: String,
    var playlistDescription: String,
    var coverUri: Uri?,
    var trackIdList: ArrayList<Long>,
    var playlistSize: Int
    )
