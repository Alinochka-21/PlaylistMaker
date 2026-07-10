package com.example.playlistmaker.settings.di.domain.repository

import com.example.playlistmaker.settings.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api_local.SettingsRepository
import org.koin.dsl.module

val settingsRepositoryModule = module {
    single <SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}