package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private var mutableState = MutableLiveData<PlaylistsState>()
    fun getPlaylistState() : LiveData<PlaylistsState> = mutableState

    init {
        viewModelScope.launch {
            interactor.getPlaylist().collect { list ->
                if (list.isNotEmpty()) {
                    mutableState.postValue(PlaylistsState.ContentState(list))
                } else {
                    mutableState.postValue(PlaylistsState.EmptyState())
                }
            }
        }
    }
}