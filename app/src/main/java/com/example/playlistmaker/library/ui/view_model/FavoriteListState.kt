package com.example.playlistmaker.library.ui.view_model

import com.example.playlistmaker.search.domain.models.Track

sealed interface FavoriteListState {

    class EmptyFavoriteList(): FavoriteListState
    class FavoriteList(val list: List<Track>): FavoriteListState
}