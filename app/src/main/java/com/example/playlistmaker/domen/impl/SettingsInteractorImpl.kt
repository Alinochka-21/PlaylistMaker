package com.example.playlistmaker.domen.impl

import com.example.playlistmaker.App
import com.example.playlistmaker.domen.api_local.SettingsInteractor
import com.example.playlistmaker.domen.api_local.SettingsRepository

class SettingsInteractorImpl (private val repository: SettingsRepository): SettingsInteractor{
    override fun getThemeMode(): Boolean {
        return repository.getThemeMode()
    }

    override fun setThemeMode(isDark: Boolean) {
        repository.setThemeMode(isDark)
        App.getInstance().switchTheme(isDark)
    }

}