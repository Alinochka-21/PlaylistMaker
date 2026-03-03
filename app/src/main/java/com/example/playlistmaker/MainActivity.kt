package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageSearch = findViewById<MaterialButton>(R.id.search)
        val imageClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?){
                val toSearchDisplay = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(toSearchDisplay)
            }
        }
        imageSearch.setOnClickListener(imageClickListener)

        val imageLibrary = findViewById<MaterialButton>(R.id.media_library)
        imageLibrary.setOnClickListener {
            val toMedia_library = Intent(this, Media_library::class.java)
            startActivity(toMedia_library)
        }

        val imageSettings = findViewById<MaterialButton>(R.id.settings)
        imageSettings.setOnClickListener {
            val toSettingsDisplay = Intent(this, SettingsActivity::class.java)
            startActivity(toSettingsDisplay)
        }


    }
}