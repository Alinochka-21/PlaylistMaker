package com.example.playlistmaker.search.ui.view_model

import com.example.playlistmaker.search.domain.models.Track

sealed interface State {
    class Default(): State
    class Content(val tracks: List<Track>): State

    class HistoryContent(val historyTracks: List<Track>): State
    class Loading(): State
    class Empty(val message: String): State
    class Error(val message: String): State
}