package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.library.data.db.AppDataBase
import com.example.playlistmaker.search.data.clients.StorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<Track>>,
    private val dataBase: AppDataBase
) : SearchHistoryRepository {

    override suspend fun getHistoryTrackList(): List<Track>{

        val keys = dataBase.getTrackDao().getPrimaryKeys()

        return storage.getData()?.onEach{ track ->
            if (keys.contains(track.trackId)) {
                track.isFavorite = true
            }
        } ?: emptyList()
    }

    override fun saveHistoryTrackList(tracks: List<Track>) {
        storage.saveData(tracks)
    }

    override fun clearTrackHistory() {
        storage.clearData()
    }
}