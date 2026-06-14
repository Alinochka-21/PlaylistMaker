package com.example.playlistmaker.domen.impl

import com.example.playlistmaker.domen.api_local.TrackHistoryInteractor
import com.example.playlistmaker.domen.api_local.TrackHistoryRepository
import com.example.playlistmaker.domen.models.Track

class TrackHistoryInteractorImpl(val historyRepository: TrackHistoryRepository) : TrackHistoryInteractor {
    override fun getTrackList(): List<Track> {
        return historyRepository.getHistoryTrackList()
    }

    override fun saveTrackList(tracks: List<Track>) {
        historyRepository.saveHistoryTrackList(tracks)
    }

    override fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    override fun clearTrackHistory() {
        historyRepository.clearTrackHistory()
    }
}