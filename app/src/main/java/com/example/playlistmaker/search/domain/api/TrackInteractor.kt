package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(searchString: String, consumer: TrackConsumer)

    interface TrackConsumer{
        fun onSuccess(tracks: List<Track>)
        fun onFailure(errorCode: Int, message: String)
    }
}