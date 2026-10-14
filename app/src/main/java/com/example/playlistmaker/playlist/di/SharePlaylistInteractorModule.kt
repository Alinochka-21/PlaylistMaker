package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.domain.impl.SharePlaylistInteractorImpl
import com.example.playlistmaker.playlist.domain.intereractor.SharePlaylistInteractor
import org.koin.dsl.module

val sharePlaylistInteractorModule =  module {
    single <SharePlaylistInteractor>{
        SharePlaylistInteractorImpl(get())
    }
}