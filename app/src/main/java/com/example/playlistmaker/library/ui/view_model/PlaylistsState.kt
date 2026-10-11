package com.example.playlistmaker.library.ui.view_model

import com.example.playlistmaker.library.domain.model.Playlist

interface PlaylistsState {
     class EmptyState() : PlaylistsState
     class ContentState(val playlists: List<Playlist>) : PlaylistsState
 }
