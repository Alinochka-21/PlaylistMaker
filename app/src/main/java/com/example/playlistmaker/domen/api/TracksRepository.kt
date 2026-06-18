package com.example.playlistmaker.domen.api

import com.example.playlistmaker.data.dto.SearchResult

interface TracksRepository {
    fun searchTracks(searchString: String): SearchResult
}