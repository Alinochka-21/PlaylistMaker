package com.example.playlistmaker.domen.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.dto.SearchResult
import com.example.playlistmaker.domen.api.TrackInteractor
import com.example.playlistmaker.domen.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(val tracksRepository: TracksRepository) : TrackInteractor{
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