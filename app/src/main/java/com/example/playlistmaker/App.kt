package com.example.playlistmaker

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.SettingsRepositoryImpl

const val  KEY_DARK_THEME = "key_dark_theme"
class App : Application() {
    var darkTheme = false

    override fun onCreate(){
        super.onCreate()
        instance = this

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val repository = SettingsRepositoryImpl(prefs)
        darkTheme = repository.getThemeMode()
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        if (darkThemeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    companion object{
        private lateinit var instance: App
        fun getInstance(): App = instance
    }
}