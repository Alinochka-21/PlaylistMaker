package com.example.playlistmaker.settings.data.repositoryImpl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.KEY_DARK_THEME
import com.example.playlistmaker.settings.domain.api_local.SettingsRepository

class SettingsRepositoryImpl (
    val context: Context,
) : SettingsRepository {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun getThemeMode(): Boolean = prefs.getBoolean(KEY_DARK_THEME, false)

    override fun setThemeMode(isDark: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_THEME, isDark) }
    }
}