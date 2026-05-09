package com.example.playlistmaker

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val  KEY_DARK_THEME = "key_dark_theme"
class App : Application() {
    var darkTheme = false

    override fun onCreate(){
        super.onCreate()

        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        darkTheme = sharedPref.getBoolean(KEY_DARK_THEME,false)
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
}