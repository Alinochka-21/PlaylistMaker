package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.KEY_DARK_THEME
import com.example.playlistmaker.domen.api_local.SettingsRepository
import androidx.core.content.edit

class SettingsRepositoryImpl (private val prefs: SharedPreferences) : SettingsRepository {

    override fun getThemeMode(): Boolean = prefs.getBoolean(KEY_DARK_THEME, false)

    override fun setThemeMode(isDark: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_THEME, isDark) }
    }
}