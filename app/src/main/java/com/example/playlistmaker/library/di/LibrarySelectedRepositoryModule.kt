package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.data.converters.TrackDbConvertor
import com.example.playlistmaker.library.data.repository_impl.SelectedTrackRepositoryImpl
import com.example.playlistmaker.library.domain.db.SelectedTracksRepository
import org.koin.dsl.module

val libraryRepositoryModule = module {

    factory {
        TrackDbConvertor()
    }

    single <SelectedTracksRepository>{
        SelectedTrackRepositoryImpl(get(),get())
    }
}