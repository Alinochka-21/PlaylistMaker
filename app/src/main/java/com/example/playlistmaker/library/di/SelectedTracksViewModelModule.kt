package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.SelectedTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val selectedTracksViewModelModule = module {
    viewModel {
        SelectedTracksViewModel(get())
    }
}