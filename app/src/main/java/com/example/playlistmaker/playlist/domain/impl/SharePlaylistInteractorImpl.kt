package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.playlist.data.navigate.ShareNavigate
import com.example.playlistmaker.playlist.domain.intereractor.SharePlaylistInteractor

class SharePlaylistInteractorImpl(private val navigator: ShareNavigate) : SharePlaylistInteractor {

    override fun sharePlaylist(info: String) {
        navigator.sharePlaylist(info)
    }
}