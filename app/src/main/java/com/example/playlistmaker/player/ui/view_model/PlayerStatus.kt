package com.example.playlistmaker.player.ui.view_model

sealed class PlayerState (val isPlayButtonEnabled: Boolean, val play: Boolean, val progress: String, var isFavorite: Boolean){
    class Default(isFavorite: Boolean) : PlayerState(false,false, "0:00", isFavorite)
    class Prepared(isFavorite: Boolean) : PlayerState(true, false, "0:00", isFavorite)
    class Playing(progress: String, isFavorite: Boolean) : PlayerState(true, true, progress, isFavorite)
    class Paused(progress: String, isFavorite: Boolean) : PlayerState(true, false, progress, isFavorite)
}