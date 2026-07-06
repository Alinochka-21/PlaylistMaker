package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.domain.api_local.TrackHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.clients.LocalDataBaseClient

class TrackHistoryRepositoryImpl(val localDataBaseClient: LocalDataBaseClient):

    TrackHistoryRepository {
    override fun getHistoryTrackList(): List<Track> {
        return localDataBaseClient.getHistoryTrackList()
    }

    override fun saveHistoryTrackList(tracks: List<Track>) {
        localDataBaseClient.saveHistoryTrackList(tracks)
    }

    override fun clearTrackHistory() {
        localDataBaseClient.clearTrackHistory()
    }

}