package com.example.playlistmaker.search.domain.models

sealed class SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult()
    data class Error(val code: Int, val message: String) : SearchResult()
}