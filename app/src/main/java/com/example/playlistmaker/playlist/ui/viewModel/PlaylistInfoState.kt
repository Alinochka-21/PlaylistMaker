package com.example.playlistmaker.playlist.ui.viewModel

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track

data class PlaylistInfoState (var playlist: Playlist, var tracks: List<Track>)