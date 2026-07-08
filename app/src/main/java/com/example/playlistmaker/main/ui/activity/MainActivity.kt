package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import com.example.playlistmaker.library.ui.activity.MediaLibraryActivity
import com.example.playlistmaker.main.ui.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewBiding: ActivityMainBinding
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewBiding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, MainViewModel.getMainViewModelFactory()).get(
            MainViewModel::class.java)

        viewModel?.getLiveTheme()?.observe(this) {
            (applicationContext as App).switchTheme(it)
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

    override fun onResume() {
        super.onResume()
        viewModel?.getThemeMode()
    }
}