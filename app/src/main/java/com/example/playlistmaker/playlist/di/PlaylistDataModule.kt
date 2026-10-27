package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.data.navigate.ShareNavigate
import com.example.playlistmaker.playlist.data.navigate_impl.ShareNavigateImpl
import org.koin.dsl.module

val shareNavigateModule = module {
    single <ShareNavigate> {
        ShareNavigateImpl(get())
    }
}