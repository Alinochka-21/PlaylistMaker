package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domen.api_local.TrackHistoryRepository
import com.example.playlistmaker.domen.models.Track

class TrackHistoryRepositoryImpl(val localDataBaseClient: LocalDataBaseClient): TrackHistoryRepository {

    override fun getHistoryTrackList(): List<Track> {
        return localDataBaseClient.getHistoryTrackDtoList().map{ fromDto(it) }
    }

    override fun saveHistoryTrackList(tracks: List<Track>) {
        val tracksDtoList = tracks.map{ toDto(it) }
        localDataBaseClient.saveHistoryTrackDtoList(tracksDtoList)
    }

    override fun addTrack(track: Track) {
        localDataBaseClient.addTrackDto(toDto(track))
    }

    override fun clearTrackHistory() {
        localDataBaseClient.clearTrackDtoHistory()
    }

    fun toDto(track: Track): TrackDto {
        return TrackDto(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.country,
            track.primaryGenreName,
            track.collectionName,
            track.releaseDate,
            track.previewUrl
        )
    }
    fun fromDto(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.trackId,
            trackDto.country,
            trackDto.primaryGenreName,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.previewUrl
        )
    }
}