package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentTrack = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(CURRENT_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CURRENT_TRACK) as? Track
        }

        val backButtonInPlayer = findViewById<ImageButton>(R.id.backButtonInPlayer).apply {
            setOnClickListener {
                finish()
            }
        }
        val trackConerOnPlayer = findViewById<ImageView>(R.id.trackConerOnPlayer).apply {
            Glide.with(context)
                .load(currentTrack!!.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.ic_default_45)
                .error(R.drawable.ic_default_45)
                .transform(RoundedCorners(dpToPx(8f, this.resources.displayMetrics).toInt()))
                .into(this)
        }

        val trackNameView = findViewById<TextView>(R.id.trackNameInPlayer).apply {
            text = currentTrack!!.trackName
        }
        val artistName = findViewById<TextView>(R.id.artistNameInPlayer).apply {
            text = currentTrack!!.artistName
        }

        val trackTime = findViewById<TextView>(R.id.trackDurationValue).apply {
            text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack!!.trackTimeMillis)
        }

        val trackCollection = findViewById<TextView>(R.id.trackAlbum)
        val trackCollectionName = findViewById<TextView>(R.id.trackAlbumValue).apply {
            if(!currentTrack!!.collectionName.isNullOrEmpty()) {
                text = currentTrack.collectionName
            } else{
                visibility = View.GONE
                trackCollection.visibility = View.GONE
            }
        }

        val trackYear = findViewById<TextView>(R.id.trackYear)
        val trackYearValue = findViewById<TextView>(R.id.trackYearValue).apply {
            if (!currentTrack!!.releaseDate.isNullOrEmpty()){
                text = currentTrack.releaseDate.take(4)
            }
            else {
                visibility = View.GONE
                trackYear.visibility = View.GONE
            }
        }
        val trackGenreName = findViewById<TextView>(R.id.trackGenreValue).apply {
            text = currentTrack!!.primaryGenreName
        }
        val trackCountry = findViewById<TextView>(R.id.trackCountryValue).apply {
            text = currentTrack!!.country
        }
    }
    companion object{
        const val CURRENT_TRACK = "CURRENT_TRACK"
    }
}