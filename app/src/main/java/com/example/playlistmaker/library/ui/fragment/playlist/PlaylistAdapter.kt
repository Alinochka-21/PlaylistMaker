package com.example.playlistmaker.library.ui.fragment.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.domain.model.Playlist

class PlaylistAdapter(private var playlists: List<Playlist>, private val onItemClick: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistTableViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistTableViewHolder {
        return PlaylistTableViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistTableViewHolder,
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