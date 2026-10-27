package com.example.playlistmaker.playlist.di

import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModel = module {
    viewModel {
        PlaylistViewModel(get(), get())
    }
}