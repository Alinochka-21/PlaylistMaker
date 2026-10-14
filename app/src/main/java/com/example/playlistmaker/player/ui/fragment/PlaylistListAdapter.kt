package com.example.playlistmaker.player.ui.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistListAdapter(private var playlists: List<Playlist>, private val onItemClick: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistListViewHolder {
        return PlaylistListViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistListViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}