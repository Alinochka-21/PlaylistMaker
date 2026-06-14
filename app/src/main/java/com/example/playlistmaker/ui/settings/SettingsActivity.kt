package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.KEY_DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.domen.api_local.SettingsInteractor
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<MaterialButton>(R.id.back).apply {
            setOnClickListener { finish() }
        }

        val  themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val interactor: SettingsInteractor = Creator.getSettingsInteractor()

        fun initThemeSwitcher() {
            val darkTheme = interactor.getThemeMode()
            themeSwitcher.isChecked = darkTheme

            themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                interactor.setThemeMode(isChecked)
            }
        }

        initThemeSwitcher()

        val shareButton = findViewById<FrameLayout>(R.id.shareAPK).apply {
            setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type ="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.practicum_website))
                startActivity(Intent.createChooser(shareIntent, "Shape APK"))
            }
        }

        val supportButton = findViewById<FrameLayout>(R.id.support).apply {
            setOnClickListener {
                val supportIntent = Intent(Intent.ACTION_SENDTO)
                supportIntent.data = Uri.parse("mailto:")
                supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
                supportIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.title_of_mail))
                supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.content_of_mail))
                startActivity(supportIntent)
            }
        }

        val agreementButton = findViewById<FrameLayout>(R.id.agreement).apply {
            setOnClickListener {
                val agreementIntent = Intent(
                    Intent.ACTION_VIEW,
                    getString(R.string.agreement_of_website).toUri()
                )
                startActivity(agreementIntent)
            }
        }
    }
}