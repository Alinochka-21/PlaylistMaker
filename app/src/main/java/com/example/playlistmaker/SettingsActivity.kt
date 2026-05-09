package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.URI
import androidx.core.net.toUri
import androidx.core.content.edit


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

        val backButtom = findViewById<com.google.android.material.button.MaterialButton>(R.id.back)
        backButtom.setOnClickListener {
            val toMainDisplay = Intent(this, MainActivity::class.java)
            finish()
        }

        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val  themeSwitcher = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPref.edit {
                putBoolean(KEY_DARK_THEME, checked)
            }
        }

        themeSwitcher.isChecked = sharedPref.getBoolean(KEY_DARK_THEME,true)

        val shareButton = findViewById<FrameLayout>(R.id.shareAPK)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type ="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.practicum_website))
            startActivity(Intent.createChooser(shareIntent, "Shape APK"))
        }

        val supportButton = findViewById<FrameLayout>(R.id.support)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.title_of_mail))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.content_of_mail))
            startActivity(supportIntent)
        }

        val agreementButton = findViewById<FrameLayout>(R.id.agreement)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW,
                getString(R.string.agreement_of_website).toUri())
            startActivity(agreementIntent)

        }
    }
}