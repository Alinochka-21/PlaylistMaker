package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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