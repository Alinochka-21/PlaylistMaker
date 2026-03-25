package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_example,parent,false)){

    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val artworkUrl100: ImageView

    init {
        trackName = itemView.findViewById(R.id.trackName)
        artistName = itemView.findViewById(R.id.artistName)
        trackTime = itemView.findViewById(R.id.trackTime)
        artworkUrl100 = itemView.findViewById(R.id.artworkUrl100)
    }

    fun bind(track: Track){
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_default_45)
            .error(R.drawable.ic_default_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.resources.displayMetrics).toInt()))
            .into(artworkUrl100)
    }
}