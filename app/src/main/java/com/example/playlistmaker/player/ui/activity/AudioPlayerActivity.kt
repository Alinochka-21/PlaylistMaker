package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel.Companion.STATE_DEFAULT
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel.Companion.STATE_PLAYING
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private val lazyCurrentTrack: Track? by lazy {
        if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(CURRENT_TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CURRENT_TRACK) as? Track
        }
    }
    val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(lazyCurrentTrack!!.previewUrl)
    }
    private lateinit var viewBiding: ActivityAudioPlayerBinding
    @SuppressLint("CheckResult")
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    var currentRunnableTime = Runnable { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBiding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audioPlayer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentTrack = lazyCurrentTrack

         val viewModel: AudioPlayerViewModel by viewModel() {
             parametersOf(currentTrack!!.previewUrl)
         }

        viewModel.getTimer().observe(this) {
            viewBiding.currentTimeTrack.text = it
        }

        viewModel.getPlayerStatus().observe(this) {
            playOrPause(it == STATE_PLAYING)
            canPressOnButton(it != STATE_DEFAULT)
        }

        viewBiding.backButtonInPlayer.setOnClickListener {
            finish()
        }

        viewBiding.trackConerOnPlayer.apply {
            Glide.with(context)
                .load(currentTrack!!.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.ic_default_45)
                .error(R.drawable.ic_default_45)
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(8f, this.resources.displayMetrics).toInt()
                    )
                )
                .into(this)
        }


        viewBiding.playTrackButton.setOnClickListener {
            viewModel.playButtonControl()
        }

        viewBiding.trackDurationValue.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack!!.trackTimeMillis)

        viewBiding.trackAlbumValue.apply {
            if (!currentTrack.collectionName.isNullOrEmpty()) {
                text = currentTrack.collectionName
            } else {
                visibility = View.GONE
                viewBiding.trackAlbum.visibility = View.GONE
            }
        }

        viewBiding.trackYearValue.apply {
            if (!currentTrack.releaseDate.isNullOrEmpty()) {
                text = currentTrack.releaseDate.take(4)
            } else {
                visibility = View.GONE
                viewBiding.trackYear.visibility = View.GONE
            }
        }

        viewBiding.trackNameInPlayer.text = currentTrack.trackName
        viewBiding.artistNameInPlayer.text = currentTrack.artistName
        viewBiding.trackGenreValue.text = currentTrack.primaryGenreName
        viewBiding.trackCountryValue.text = currentTrack.country
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(currentRunnableTime)
    }

    private fun playOrPause(play: Boolean) {
        if (play) {
            viewBiding.playTrackButton.setImageResource(R.drawable.ic_pause_83)
        } else {
            viewBiding.playTrackButton.setImageResource(R.drawable.ic_play_83)
        }
    }

    private fun canPressOnButton(canPress: Boolean){
        viewBiding.playTrackButton.isEnabled = canPress
    }

    companion object {
       private const val CURRENT_TRACK = "CURRENT_TRACK"
    }
}