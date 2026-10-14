package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistListExampleBinding
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistListViewHolder(private val binding: PlaylistListExampleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.playlistNamePlace.text = playlist.playlistName
        binding.playlistSizePlace.text = playlist.trackIdList.size.toString() + wordEnding(playlist.trackIdList.size)
        if (playlist.coverUri == null) {
            binding.playlistCoverPlace.setImageResource(R.drawable.ic_default_45)
        } else {
            binding.playlistCoverPlace.setImageURI(playlist.coverUri)
        }
    }

    private fun wordEnding(count: Int): String{
        return when(count) {
            1 ->  binding.root.context.getString(R.string.one)
            in (2..4) -> binding.root.context.getString(R.string.two_to_four)
            else -> binding.root.context.getString(R.string.from_four)
        }
    }

    companion object {
        fun from(parent: ViewGroup) : PlaylistListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistListExampleBinding.inflate(inflater,parent,false)
            return PlaylistListViewHolder(binding)
        }
    }
}