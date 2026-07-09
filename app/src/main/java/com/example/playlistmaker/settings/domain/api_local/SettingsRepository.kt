package com.example.playlistmaker.settings.domain.api_local

interface SettingsRepository {
    fun getThemeMode(): Boolean
    fun setThemeMode(isDark: Boolean)
}