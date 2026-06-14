package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.local_data_source.SharedPrefLocalBaseClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domen.impl.TracksInteractorImpl
import com.example.playlistmaker.domen.api.TrackInteractor
import com.example.playlistmaker.domen.api.TracksRepository
import com.example.playlistmaker.domen.api_local.SettingsInteractor
import com.example.playlistmaker.domen.api_local.SettingsRepository
import com.example.playlistmaker.domen.api_local.TrackHistoryInteractor
import com.example.playlistmaker.domen.api_local.TrackHistoryRepository
import com.example.playlistmaker.domen.impl.SettingsInteractorImpl
import com.example.playlistmaker.domen.impl.TrackHistoryInteractorImpl

object Creator {
     fun getTracksRepository(): TracksRepository{
         return TrackRepositoryImpl(RetrofitNetworkClient())
     }

    fun getTracksInteractor(): TrackInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getTrackHistoryRepository(): TrackHistoryRepository{
        return TrackHistoryRepositoryImpl(SharedPrefLocalBaseClient(getSharedPreferences()))
    }

    fun getTrackHistoryInteractor(): TrackHistoryInteractor{
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }

    private fun getSharedPreferences(): SharedPreferences {
        val context = App.getInstance()
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPreferences())
    }

    fun getSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
}