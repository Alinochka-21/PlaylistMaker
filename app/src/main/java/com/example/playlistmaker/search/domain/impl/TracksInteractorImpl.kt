package com.example.playlistmaker.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResult
import java.util.concurrent.Executors

class TracksInteractorImpl(val tracksRepository: TracksRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    private val uiHandler = Handler(Looper.getMainLooper())

    override fun searchTracks(searchString: String, consumer: TrackInteractor.TrackConsumer){

        executor.execute {
            val searchResult = tracksRepository.searchTracks(searchString)
            uiHandler.post {
                when (searchResult){
                    is SearchResult.Success -> consumer.onSuccess(searchResult.tracks)
                    is SearchResult.Error -> consumer.onFailure(searchResult.code, searchResult.message)
                }
            }
        }
    }
}