package com.example.playlistmaker.playlistCreator.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    fun addPlaylist(name: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            val playlist = Playlist(
                playlistName = name,
                playlistDescription = description,
                coverUri = uri,
                trackIdList = arrayListOf(),
                playlistSize = 0,
                playlistId = 0
            )

            interactor.addPlaylist(playlist)
        }
    }
}