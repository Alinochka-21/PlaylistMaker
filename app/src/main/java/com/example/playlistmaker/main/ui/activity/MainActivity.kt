package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.KEY_DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.library.ui.activity.MediaLibraryActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewBiding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkTheme = sharedPref.getBoolean(KEY_DARK_THEME,true)
        (applicationContext as App).switchTheme(isDarkTheme)

        super.onCreate(savedInstanceState)
        viewBiding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBiding.search.setOnClickListener{
            val toSearchDisplay = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(toSearchDisplay)
        }

        viewBiding.mediaLibrary.setOnClickListener {
            val toMediaLibrary = Intent(this, MediaLibraryActivity::class.java)
            startActivity(toMediaLibrary)
        }

        viewBiding.settings.setOnClickListener {
            val toSettingsDisplay = Intent(this, SettingsActivity::class.java)
            startActivity(toSettingsDisplay)
        }
    }
}