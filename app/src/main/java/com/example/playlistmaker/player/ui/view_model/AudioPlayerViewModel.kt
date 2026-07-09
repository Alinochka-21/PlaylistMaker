package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(val url: String) : ViewModel() {

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(url)
            }
        }
    }

    private var liveTimer = MutableLiveData("0:00")
    fun getTimer(): LiveData<String> = liveTimer

    private var livePlayerStatus = MutableLiveData(STATE_DEFAULT)
    fun getPlayerStatus(): LiveData<Int> = livePlayerStatus
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    var timerRunnable = Runnable {
        if (livePlayerStatus.value == STATE_PLAYING) {
            startTimer()
        }
    }

    init {
        prepareMediaPlayer()
    }

    fun prepareMediaPlayer(){
        mediaPlayer.apply{
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener{
                livePlayerStatus.postValue(STATE_PREPARED)
            }
            setOnCompletionListener{
                livePlayerStatus.postValue(STATE_PREPARED)
                resetTimer()
            }
        }
    }

    private fun playTrack(){
        mediaPlayer.start()
        livePlayerStatus.postValue(STATE_PLAYING)
        startTimer()
    }

    private fun pauseTrack(){
        pauseTimer()
        mediaPlayer.pause()
        livePlayerStatus.postValue(STATE_PAUSED)
    }

    fun onPause() {
        pauseTrack()
    }

    fun playButtonControl(){
        when (livePlayerStatus.value){
            STATE_PLAYING -> pauseTrack()
            STATE_PREPARED, STATE_PAUSED -> playTrack()
        }
    }

    private fun startTimer(){
        liveTimer.postValue(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()).format(mediaPlayer.currentPosition
                )
        )
        handler.postDelayed(timerRunnable,500)
    }

    private fun pauseTimer(){
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer(){
        handler.removeCallbacks(timerRunnable)
        liveTimer.postValue("0:00")
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }
}