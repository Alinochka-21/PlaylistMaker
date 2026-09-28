package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.domain.db.SelectedTrackInteractor
import com.example.playlistmaker.library.domain.impl.SelectedTrackInteractorImpl
import org.koin.dsl.module

val selectedTrackInteractorModule = module {
    single<SelectedTrackInteractor> {
        SelectedTrackInteractorImpl(get())
    }
}