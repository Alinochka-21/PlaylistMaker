package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.repository_impl.PlaylistRepositoryImpl
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.google.gson.Gson
import org.koin.dsl.module

val playlistRepositoryModule = module {

    factory {
        Gson()
    }

    factory {
        PlaylistDbConverter(get())
    }

    single <PlaylistRepository>{
        PlaylistRepositoryImpl(get(), get())
    }
}