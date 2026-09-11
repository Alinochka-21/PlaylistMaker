package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val context: Context,
    private val tracksInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
    ) : ViewModel() {

    private var latestSearchText: String = ""
    private val stateLiveData = MutableLiveData<State>()
    fun getStateLiveData(): LiveData<State> = stateLiveData

    private var isTrackEnable = MutableLiveData<Boolean>(true)
    fun getTrackEnable(): LiveData<Boolean> = isTrackEnable
    private var debounceJob : Job? = null
    private var clickJob: Job? = null

    private var historyTrackList: ArrayList<Track> = getHistoryTrackList()

    fun searchDebounce(textChanged: String){
        if (latestSearchText == textChanged){
            return
        }

        latestSearchText = textChanged

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            performSearch(textChanged)
        }
    }

    fun performSearch(newSearchText: String){

        if (newSearchText.isNotEmpty()) {

            latestSearchText = newSearchText

           postState(State.Loading())

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first,pair.second)
                    }
            }
        } else {
            setBeginningState()
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                postState(
                    State.Error(context.getString(R.string.not_internet))
                )
            }
            tracks.isEmpty() -> {
                postState(
                    State.Empty(context.getString(R.string.not_found))
                )
            }
            else -> {
                postState(
                    State.Content(tracks)
                )
            }
        }

    }
    fun setBeginningState(){
        debounceJob?.cancel()

        if (historyTrackList.isEmpty) {
            postState(
                State.Default()
            )
        } else {
            postState(
                State.HistoryContent(historyTrackList)
            )
        }
    }

    fun retryLastRequest(){
        if (latestSearchText.isNotEmpty()){
            performSearch(latestSearchText)
        }
    }

    fun  onFocusChanged(hasFocus: Boolean, query: String) {
        val state = if (hasFocus && query.isEmpty() && historyTrackList.isNotEmpty()) {
            State.HistoryContent(historyTrackList)
        } else {
            State.Default()
        }
        postState(state)
    }

    fun postState(state: State){
        stateLiveData.postValue(state)
    }

    fun clearHistoryTrackList(){
        searchHistoryInteractor.clearTrackHistory()
        postState(
            State.Default()
        )
    }

    fun addHistoryTrackList(track: Track){
        searchHistoryInteractor.addTrack(track)
        historyTrackList = ArrayList(searchHistoryInteractor.getTrackList())
    }

    fun getHistoryTrackList(): ArrayList<Track> {
        historyTrackList = ArrayList(searchHistoryInteractor.getTrackList())
        return  historyTrackList
    }

    fun clickDebounce(){
        isTrackEnable.postValue(false)
        clickJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isTrackEnable.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        clickJob?.cancel()
        clickJob = null
        debounceJob?.cancel()
        debounceJob = null
    }

    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}