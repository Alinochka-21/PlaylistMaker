package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.SearchResult
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(searchString: String): Flow<SearchResult>
}