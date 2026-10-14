package com.example.playlistmaker.playlistEdit.di

import com.example.playlistmaker.playlistEdit.ui.view_model.PlaylistEditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editViewModelModule = module {

     viewModel{
         PlaylistEditViewModel(get())
     }
 }