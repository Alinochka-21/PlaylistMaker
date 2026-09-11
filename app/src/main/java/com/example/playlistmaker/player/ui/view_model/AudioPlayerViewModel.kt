package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(val url: String) : ViewModel() {
    private var livePlayerStatus = MutableLiveData<PlayerState>(PlayerState.Default())
    fun getPlayerStatus(): LiveData<PlayerState> = livePlayerStatus
    private val mediaPlayer = MediaPlayer()

    var timerJob: Job? = null

    init {
        prepareMediaPlayer()
    }

    fun prepareMediaPlayer(){
        mediaPlayer.apply{
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener{
                livePlayerStatus.postValue(PlayerState.Prepared())
            }
            setOnCompletionListener{
                timerJob?.cancel()
                timerJob = null
                livePlayerStatus.postValue(PlayerState.Prepared())
            }
        }
    }
    private fun startTimer(){
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying){
                delay(300L)
                livePlayerStatus.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }
    private fun playTrack(){
        mediaPlayer.start()
        livePlayerStatus.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }
    private fun pauseTrack(){
        mediaPlayer.pause()
        timerJob?.cancel()
        livePlayerStatus.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun onPause() {
        pauseTrack()
    }

    fun playButtonControl(){
        when (livePlayerStatus.value){
            is PlayerState.Playing -> pauseTrack()
            is PlayerState.Prepared, is PlayerState.Paused -> playTrack()
            else -> return
        }
    }

    private fun getCurrentPlayerPosition(): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.stop()
        mediaPlayer.release()
        livePlayerStatus.value = PlayerState.Default()
    }
}