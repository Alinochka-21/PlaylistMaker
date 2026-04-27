package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        Log.d("search", "мы в мейнактивити значение дарк темы: ${sharedPref.getBoolean(KEY_DARK_THEME,(applicationContext as App).darkTheme)}")
        if (sharedPref.getBoolean(KEY_DARK_THEME,true)){
            (applicationContext as App).switchTheme(true)
        } else {
            (applicationContext as App).switchTheme(false)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageSearch = findViewById<MaterialButton>(R.id.search)
        imageSearch.setOnClickListener{
            val toSearchDisplay = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(toSearchDisplay)
        }
        
        val imageLibrary = findViewById<MaterialButton>(R.id.media_library)
        imageLibrary.setOnClickListener {
            val toMediaLibrary = Intent(this, Media_library::class.java)
            startActivity(toMediaLibrary)
        }

        val imageSettings = findViewById<MaterialButton>(R.id.settings)
        imageSettings.setOnClickListener {
            val toSettingsDisplay = Intent(this, SettingsActivity::class.java)
            startActivity(toSettingsDisplay)
        }
    }
}