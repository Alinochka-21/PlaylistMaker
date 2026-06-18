package com.example.playlistmaker.data

import android.util.Log
import com.example.playlistmaker.data.dto.SearchResult
import com.example.playlistmaker.domen.models.Track
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domen.api.TracksRepository

class TrackRepositoryImpl(private val netWorkClient: NetWorkClient) : TracksRepository{

    override fun searchTracks(searchString: String): SearchResult{

        try {
            val response = netWorkClient.doRequest(TracksSearchRequest(searchString))

            if (response.resultCode == 200) {
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
                return SearchResult.Success(tracks)
            } else {
                return SearchResult.Error(response.resultCode, "Код не 200")
            }
        } catch (e: Exception){
           return SearchResult.Error(-1, "Запрос не дошел до сервера. Проблема с Итернетом")
        }
    }
}