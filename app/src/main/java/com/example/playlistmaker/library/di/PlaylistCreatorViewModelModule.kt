package com.example.playlistmaker.library.di

import com.example.playlistmaker.playlistCreator.ui.view_model.PlaylistCreatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistCreatorViewModelModule = module {
    viewModel {
        PlaylistCreatorViewModel(get())
    }
}