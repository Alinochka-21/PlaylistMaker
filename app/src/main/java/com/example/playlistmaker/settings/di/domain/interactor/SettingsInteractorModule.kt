package com.example.playlistmaker.settings.di.domain.interactor

import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingsInteractorModule = module {
    single <SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}