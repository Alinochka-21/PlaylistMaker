package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domen.models.Track

sealed class SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult()
    data class Error(val code: Int, val message: String) : SearchResult()
}
