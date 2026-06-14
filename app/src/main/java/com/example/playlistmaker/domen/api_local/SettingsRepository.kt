package com.example.playlistmaker.domen.api_local

interface SettingsRepository {
    fun getThemeMode(): Boolean
    fun setThemeMode(isDark: Boolean)
}