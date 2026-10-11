package com.example.playlistmaker.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.SelectedTrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class AudioPlayerViewModel(val url: String, val isFavorite: Boolean, val selectedTrackInteractor: SelectedTrackInteractor) : ViewModel() {
    private var livePlayerStatus = MutableLiveData<PlayerState>(PlayerState.Default(isFavorite))
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
                livePlayerStatus.postValue(PlayerState.Prepared(isFavorite))
            }
            setOnCompletionListener{
                timerJob?.cancel()
                timerJob = null
                livePlayerStatus.postValue(PlayerState.Prepared(isFavorite))
            }
        }
    }
    private fun startTimer(){
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying){
                delay(300L)
                livePlayerStatus.postValue(PlayerState.Playing(getCurrentPlayerPosition(),isFavorite))
            }
        }
    }
    private fun playTrack(){
        mediaPlayer.start()
        livePlayerStatus.postValue(PlayerState.Playing(getCurrentPlayerPosition(),isFavorite))
        startTimer()
    }
    private fun pauseTrack(){
        mediaPlayer.pause()
        timerJob?.cancel()
        livePlayerStatus.postValue(PlayerState.Paused(getCurrentPlayerPosition(), isFavorite))
    }
    fun onPause(){
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
        return try {
            val position = mediaPlayer.currentPosition
            val minutes = position / 1000 / 60
            val seconds = position / 1000 % 60
            String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
        } catch (e: IllegalStateException) {
            "0:00"
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.stop()
        mediaPlayer.release()
        livePlayerStatus.value = PlayerState.Default(isFavorite)
    }

    fun onFavoriteClicked(track: Track){

        viewModelScope.launch {
        if (!track.isFavorite){
            selectedTrackInteractor.deleteSelectedTrack(track)
            val currentState = livePlayerStatus.value
            currentState?.isFavorite = false
            livePlayerStatus.postValue(currentState!!)
        } else {
            selectedTrackInteractor.addSelectedTrack(track)
            val currentState = livePlayerStatus.value
            currentState?.isFavorite = true
            livePlayerStatus.postValue(currentState!!)
        }
        }
    }
}