package com.example.playlistmaker.player.ui.view_model

import com.example.playlistmaker.library.domain.model.Playlist

sealed class BottomSheetState () {

    class AlreadyState(val name: String) : BottomSheetState()
    class Success(val name: String, val success: Boolean) : BottomSheetState()
}
