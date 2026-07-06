package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.api_local.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api_local.TrackHistoryRepository

class TrackHistoryInteractorImpl(val historyRepository: TrackHistoryRepository) :
    TrackHistoryInteractor {
    override fun getTrackList(): List<Track> {
        return historyRepository.getHistoryTrackList()
    }

    override fun saveTrackList(tracks: List<Track>) {
        historyRepository.saveHistoryTrackList(tracks)
    }

    override fun addTrack(track: Track) {
        val trackHistory = historyRepository.getHistoryTrackList().toMutableList()

        trackHistory.apply{
            removeAll{it.trackId == track.trackId}
            add(0, track)
        }

        if (trackHistory.size > MAX_TRACK_COUNT){
            trackHistory.removeAt(trackHistory.size-1)
        }

       saveTrackList(trackHistory)
    }

    override fun clearTrackHistory() {
        historyRepository.clearTrackHistory()
    }
    companion object {
        private const val MAX_TRACK_COUNT = 10
    }
}