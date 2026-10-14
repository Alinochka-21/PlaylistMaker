package com.example.playlistmaker.library.ui.fragment.playlist

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistTableExampleBinding
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistTableViewHolder(private val binding: PlaylistTableExampleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.playlistNameInRecycler.text = playlist.playlistName
        binding.tracksCountInRecycler.text = playlist.trackIdList.size.toString() + wordEnding(playlist.trackIdList.size)
        if (playlist.coverUri == null) {
            binding.playlistCoverInRecycler.setImageResource(R.drawable.img_empty_cover_103)
            binding.playlistCoverInRecycler.scaleType = ImageView.ScaleType.CENTER
        } else {
            binding.playlistCoverInRecycler.setImageURI(playlist.coverUri)
            binding.playlistCoverInRecycler.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun wordEnding(count: Int): String{
        return when(count) {
            1 ->  binding.root.context.getString(R.string.one)
            in (2..4) -> binding.root.context.getString(R.string.two_to_four)
            else -> binding.root.context.getString(R.string.from_four)
        }
    }

    companion object{
        fun from(parent: ViewGroup): PlaylistTableViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistTableExampleBinding.inflate(inflater,parent,false)
            return PlaylistTableViewHolder(binding)
        }
    }
}