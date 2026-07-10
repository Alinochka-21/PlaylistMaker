package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import org.koin.dsl.module

val sharingInteractorModule = module {

        single <SharingInteractor> {
            SharingInteractorImpl(get())
        }
}