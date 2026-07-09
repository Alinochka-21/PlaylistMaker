package com.example.playlistmaker.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val context: Context) : ViewModel() {

    private val tracksInteractor = Creator.getTracksInteractor()
    private val searchHistoryInteractor = Creator.getSearchHistoryInteractor()
    private var latestSearchText: String = ""
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<State>()
    fun getStateLiveData(): LiveData<State> = stateLiveData

    private var isTrackEnable = MutableLiveData<Boolean>(true)
    fun getTrackEnable(): LiveData<Boolean> = isTrackEnable

    private var historyTrackList: ArrayList<Track> = getHistoryTrackList()

    fun searchDebounce(textChanged: String){

        if (latestSearchText == textChanged){
            return
        }

        latestSearchText = textChanged

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable {performSearch(textChanged)}

        val time = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, time)
    }


    fun performSearch(newSearchText: String){

        if (newSearchText.isNotEmpty()) {

            latestSearchText = newSearchText
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

            postState(State.Loading())

            tracksInteractor.searchTracks(newSearchText, object : TrackInteractor.TrackConsumer {

                override fun onSuccess(tracks: List<Track>) {
                    handler.post {
                        if (tracks.isNotEmpty()) {
                            postState(
                                State.Content(tracks)
                            )
                        } else {
                            postState(
                                State.Empty(context.getString(R.string.not_found))
                            )
                        }
                    }
                }

                override fun onFailure(errorCode: Int, message: String) {
                    handler.post {
                        postState(
                            State.Error(context.getString(R.string.not_internet))
                        )
                    }
                }
            })
        } else {
            setBeginningState()
        }
    }

    fun setBeginningState(){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

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
        handler.postAtTime(
            {isTrackEnable.value = true},
            CLICK_DEBOUNCE_TOKEN,
            SystemClock.uptimeMillis() + CLICK_DEBOUNCE_DELAY
        )
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        handler.removeCallbacksAndMessages(CLICK_DEBOUNCE_TOKEN)
    }

    companion object{

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
        private val CLICK_DEBOUNCE_TOKEN = Any()

        fun getSearchFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SearchViewModel(app)
            }
        }
    }
}