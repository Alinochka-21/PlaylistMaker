package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto

interface LocalDataBaseClient {

    fun getHistoryTrackDtoList(): List<TrackDto>

    fun saveHistoryTrackDtoList(tracksDto: List<TrackDto>)

    fun clearTrackDtoHistory()
}