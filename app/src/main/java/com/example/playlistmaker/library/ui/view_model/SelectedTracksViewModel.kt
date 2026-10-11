package com.example.playlistmaker.library.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.SelectedTrackInteractor
import kotlinx.coroutines.launch

class SelectedTracksViewModel(private val selectedTrackInteractor: SelectedTrackInteractor) : ViewModel() {

    private val mutableState = MutableLiveData<FavoriteListState>()
    fun getFavoriteListState(): LiveData<FavoriteListState> = mutableState

    init {
        viewModelScope.launch {
            selectedTrackInteractor.getSelectedList().collect { list ->
                if (list.isNotEmpty()){
                    mutableState.postValue(FavoriteListState.FavoriteList(list))
                } else {
                    mutableState.postValue(FavoriteListState.EmptyFavoriteList())
                }
            }
        }
    }
}
