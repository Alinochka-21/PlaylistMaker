package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {

    fun mapToTrack(trackEntity: TrackEntity): Track{
        return Track(
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            trackId = trackEntity.trackId,
            country = trackEntity.country,
            primaryGenreName = trackEntity.primaryGenreName,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            previewUrl = trackEntity.previewUrl
        )
    }

    fun mapToTrackEntity(track: Track): TrackEntity{
        return TrackEntity(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl
        )
    }
}