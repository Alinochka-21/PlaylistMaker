package com.example.playlistmaker.playlist.data.navigate_impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.playlist.data.navigate.ShareNavigate

class ShareNavigateImpl(private val context: Context) : ShareNavigate {

    override fun sharePlaylist(info: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, info)
        }
        val chooser = Intent.createChooser(shareIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}