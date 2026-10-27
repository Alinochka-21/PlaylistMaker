package com.example.playlistmaker.playlist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.playlist.domain.intereractor.SharePlaylistInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class PlaylistViewModel(private val interactor: PlaylistInteractor, private val navigateInteractor: SharePlaylistInteractor) : ViewModel() {
    private var mutableState : MutableLiveData<PlaylistInfoState> = MutableLiveData()
    private lateinit var playlist: Playlist
    private lateinit var trackList: List<Track>
    private var collectJob: Job? = null

    fun getState() : LiveData<PlaylistInfoState> = mutableState

    fun getPlaylistInfo(id: Long) {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            val currentPlaylist = interactor.getPlaylistInfo(id)
            playlist = currentPlaylist
            interactor.getTracksFromPlaylist(currentPlaylist.trackIdList).collect { tracks ->
                trackList = tracks
                mutableState.postValue(PlaylistInfoState(currentPlaylist,tracks))
            }
        }
    }
    fun deleteTrack(idPlaylist: Long, idTrack: Long){
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(idPlaylist, idTrack)
            getPlaylistInfo(idPlaylist)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            interactor.deletePlaylist(playlist.playlistId, playlist.trackIdList)
        }
    }
    fun sharePlaylist(playlistInfo: String) {
        navigateInteractor.sharePlaylist(playlistInfo)
    }

    fun getCurrentPlaylist() : Playlist {
        return playlist
    }

    fun getCurrentTrackList() : List<Track> {
        return trackList
    }
}