package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.search.data.repository_impl.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.repository_impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.settings.domain.api_local.SettingsRepository
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.google.gson.reflect.TypeToken

object Creator {
    private fun getSharedPreferences(): SharedPreferences {
        val context = App.Companion.getInstance()
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
     fun getTracksRepository(): TracksRepository {
         return TrackRepositoryImpl(RetrofitNetworkClient())
     }

    fun getTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPreferences())
    }

    fun getSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun getExternalNavigator(context: Context): ExternalNavigator {
       return ExternalNavigatorImpl(context)
    }
    fun getSharingInteractor(context: Context): SharingInteractor {
        val navigator = getExternalNavigator(context)
        return SharingInteractorImpl(navigator, context)
    }
    private fun getSearchHistoryRepository(): SearchHistoryRepository = SearchHistoryRepositoryImpl(
        PrefsStorageClient<List<Track>>(
            getSharedPreferences(),
            KEY_TRACK_HISTORY,
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    )

    fun getSearchHistoryInteractor(): SearchHistoryInteractor = SearchHistoryInteractorImpl(
        getSearchHistoryRepository())

    private const val KEY_TRACK_HISTORY = "search_history"
}