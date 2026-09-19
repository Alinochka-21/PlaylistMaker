package com.example.playlistmaker.library.data.repository_impl

import com.example.playlistmaker.library.data.converters.TrackDbConvertor
import com.example.playlistmaker.library.data.db.AppDataBase
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.domain.db.SelectedTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectedTrackRepositoryImpl(
    private val convertor: TrackDbConvertor,
    private val dataBase: AppDataBase
) : SelectedTracksRepository{

    override suspend fun addSelectedTrack(track: Track){
        val trackEntity = convertor.mapToTrackEntity(track)
        dataBase.getTrackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteSelectedTrack(track: Track) {
        val trackEntity = convertor.mapToTrackEntity(track)
        dataBase.getTrackDao().deleteTrack(trackEntity)
    }

    override fun getSelectedList(): Flow<List<Track>> = flow {
        val tracksEntity = dataBase.getTrackDao().getTracks()
        emit(convertToTrackList(tracksEntity))
    }

    private fun convertToTrackList(tracksEntity: List<TrackEntity>): List<Track>{
        return tracksEntity.map { track -> convertor.mapToTrack(track)
        }
    }
}