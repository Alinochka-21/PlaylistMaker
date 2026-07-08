package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private var viewModel: SettingsViewModel? = null
    private lateinit var viewBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, SettingsViewModel.getFactory()).get(SettingsViewModel::class.java)

        viewModel?.getTheme()?.observe(this){
            initThemeSwitcher(it)
        }

        viewBinding.backButton.setOnClickListener { finish() }

        viewBinding.shareButton.setOnClickListener {
            val intent = viewModel?.share()
            startActivity(intent)
        }

        viewBinding.supportButton.setOnClickListener {
            val intent = viewModel?.support()
            startActivity(intent)
        }

        viewBinding.agreementButton.setOnClickListener {
            val intent = viewModel?.agreement()
            startActivity(intent)
        }
    }
    fun initThemeSwitcher(darkTheme: Boolean) {
        viewBinding.themeSwitcher.isChecked = darkTheme
        viewBinding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel?.changeTheme()
        }
    }
}