package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResult
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(val tracksRepository: TracksRepository) : TrackInteractor {

    override fun searchTracks(searchString: String): Flow<Pair<List<Track>?, String?>> {

        return tracksRepository.searchTracks(searchString).map { result ->
            when (result) {
                is SearchResult.Success -> {
                    Pair(result.tracks,null)
                }
                is SearchResult.Error -> {
                    Pair(null,result.message)
                }
            }
        }
    }
}