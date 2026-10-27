package com.example.playlistmaker.playlistEdit.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.playlistCreator.ui.view_model.PlaylistCreatorViewModel
import kotlinx.coroutines.launch

class PlaylistEditViewModel(interactor: PlaylistInteractor) : PlaylistCreatorViewModel(interactor) {
    private var mutableState : MutableLiveData<EditPlaylistState> = MutableLiveData()
    fun getState() : LiveData<EditPlaylistState> = mutableState
    private lateinit var playlist: Playlist

    private fun getOldInformation(id: Long){

        viewModelScope.launch {
            playlist = interactor.getPlaylistInfo(id)
            mutableState.postValue(EditPlaylistState(
                playlist.playlistName,
                playlist.coverUri,
                playlist.playlistDescription)
            )
        }
    }

    fun getPlaylistId(id: Long) {
        getOldInformation(id)
    }

    fun saveNewInformation(
        newName: String,
        newCover: Uri?,
        newDescription: String?
    ) {
        viewModelScope.launch {
            interactor.changePlaylist(
                playlist.playlistId,
                newName,
                newCover,
                newDescription ?: "")
        }
    }
}