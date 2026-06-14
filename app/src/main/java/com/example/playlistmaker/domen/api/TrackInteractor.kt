package com.example.playlistmaker.domen.api

import com.example.playlistmaker.domen.models.Track

interface TrackInteractor {
    fun searchTracks(searchString: String, consumer: TrackConsumer)

    interface TrackConsumer{
        fun onSuccess(tracks: List<Track>)
        fun onFailure(errorCode: Int, message: String)
    }
}