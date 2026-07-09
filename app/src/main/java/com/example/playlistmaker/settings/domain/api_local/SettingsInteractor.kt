package com.example.playlistmaker.settings.domain.api_local

interface SettingsInteractor {

    fun getThemeMode(): Boolean

    fun setThemeMode(isDark: Boolean)
}