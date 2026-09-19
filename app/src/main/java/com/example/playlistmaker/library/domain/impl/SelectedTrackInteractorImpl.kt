package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.SelectedTrackInteractor
import com.example.playlistmaker.library.domain.db.SelectedTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedTrackInteractorImpl(
    private val selectedTracksRepository: SelectedTracksRepository
) : SelectedTrackInteractor {

    override suspend fun addSelectedTrack(track: Track) {
        selectedTracksRepository.addSelectedTrack(track)
    }

    override suspend fun deleteSelectedTrack(track: Track) {
        selectedTracksRepository.deleteSelectedTrack(track)
    }

    override fun getSelectedList(): Flow<List<Track>> {
        val trackList = selectedTracksRepository.getSelectedList()
        return trackList.map { tracks -> tracks.reversed() }
    }
}