package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.TypedValueCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackExampleBinding
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackExampleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track){
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_default_45)
            .error(R.drawable.ic_default_45)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValueCompat.dpToPx(2f, itemView.resources.displayMetrics).toInt()
                )
            )
            .into(binding.artworkUrl100)
    }
    companion object{
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackExampleBinding.inflate(inflater,parent,false)
            return TrackViewHolder(binding)
        }
    }
}