package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.KEY_DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.library.ui.activity.MediaLibraryActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val isDarkTheme = sharedPref.getBoolean(KEY_DARK_THEME,true)
        (applicationContext as App).switchTheme(isDarkTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageSearch = findViewById<MaterialButton>(R.id.search)
        imageSearch.setOnClickListener{
            val toSearchDisplay = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(toSearchDisplay)
        }

        val imageLibrary = findViewById<MaterialButton>(R.id.media_library)
        imageLibrary.setOnClickListener {
            val toMediaLibrary = Intent(this, MediaLibraryActivity::class.java)
            startActivity(toMediaLibrary)
        }

        val imageSettings = findViewById<MaterialButton>(R.id.settings)
        imageSettings.setOnClickListener {
            val toSettingsDisplay = Intent(this, SettingsActivity::class.java)
            startActivity(toSettingsDisplay)
        }
    }
}