package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domen.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    private val mediaPlayer = MediaPlayer()
    lateinit var playTrackButton: ImageButton
    lateinit var currentTimeTrack: TextView
    private var playerStatus = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    var currentRunnableTime = Runnable {  }

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
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(8f, this.resources.displayMetrics).toInt()
                    )
                )
                .into(this)
        }

        fun prepareMediaPlayer(){
            mediaPlayer.apply{
                setDataSource(currentTrack?.previewUrl)
                prepareAsync()
                setOnPreparedListener{
                    playTrackButton.isEnabled = true
                    playerStatus = STATE_PREPARED
                    currentTimeTrack.text=DEFAULT_TIME
                }
                setOnCompletionListener{
                    playTrackButton.setImageResource(R.drawable.ic_play_83)
                    playerStatus = STATE_PREPARED
                    handler.removeCallbacks (currentRunnableTime)
                    currentTimeTrack.text = DEFAULT_TIME
                }
            }
        }

        prepareMediaPlayer()

        playTrackButton = findViewById<ImageButton>(R.id.playTrackButton).apply {
            setOnClickListener {
                playTrackButtonControl()
            }
        }

        currentTimeTrack = findViewById<TextView>(R.id.currentTimeTrack)

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

    override fun onPause() {
        super.onPause()
        pauseTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(currentRunnableTime)
    }
    private fun playTrack(){
        mediaPlayer.start()
        playTrackButton.setImageResource(R.drawable.ic_pause_83)
        playerStatus = STATE_PLAYING
        startTimer()
        handler.postDelayed(currentRunnableTime, 0)
    }


    private fun pauseTrack(){
        mediaPlayer.pause()
        playTrackButton.setImageResource(R.drawable.ic_play_83)
        playerStatus = STATE_PAUSED
        handler.removeCallbacks (currentRunnableTime)
    }

    private fun playTrackButtonControl(){
        when (playerStatus){
            STATE_PLAYING -> pauseTrack()
            STATE_PREPARED, STATE_PAUSED -> playTrack()
        }
    }

    private fun startTimer(){
        currentRunnableTime = object : kotlinx.coroutines.Runnable {
            override fun run() {
                currentTimeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, 500)
            }
        }
    }

    companion object{
        const val CURRENT_TRACK = "CURRENT_TRACK"

        private const val STATE_DEFAULT = 0

        private const val STATE_PREPARED = 1

        private const val STATE_PLAYING = 2

        private const val STATE_PAUSED = 3

        private const val DEFAULT_TIME = "00:00"
    }
}