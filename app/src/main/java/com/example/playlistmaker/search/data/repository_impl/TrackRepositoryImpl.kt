package com.example.playlistmaker.search.data.repository_impl

import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.clients.NetWorkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.models.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val netWorkClient: NetWorkClient) : TracksRepository {

    override fun searchTracks(searchString: String): Flow<SearchResult> = flow {

            val response = netWorkClient.doRequest(TracksSearchRequest(searchString))

             when (response.resultCode) {
                 200 -> {
                     val tracks = (response as TracksSearchResponse).results.map {
                         Track(
                             it.trackName,
                             it.artistName,
                             it.trackTimeMillis,
                             it.artworkUrl100,
                             it.trackId,
                             it.country,
                             it.primaryGenreName,
                             it.collectionName,
                             it.releaseDate,
                             it.previewUrl
                         )
                     }
                     emit(SearchResult.Success(tracks))
                 }

                 else -> {
                     emit(SearchResult.Error(response.resultCode, "Код не 200"))
                 }
             }
    }
}