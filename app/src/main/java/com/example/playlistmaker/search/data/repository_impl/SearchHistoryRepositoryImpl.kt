package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.data.clients.StorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<Track>>) : SearchHistoryRepository {

    override fun getHistoryTrackList(): List<Track> {
        return storage.getData() ?: emptyList()
    }

    override fun saveHistoryTrackList(tracks: List<Track>) {
        storage.saveData(tracks)
    }

    override fun clearTrackHistory() {
        storage.clearData()
    }
}