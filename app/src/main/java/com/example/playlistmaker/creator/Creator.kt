package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.settings.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.settings.domain.api_local.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

/*
object Creator {
    fun getTracksRepository(): TracksRepository {
         return TrackRepositoryImpl(RetrofitNetworkClient())
     }



    fun getTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(App.Companion.getInstance())
    }

    fun getSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun getExternalNavigator(context: Context): ExternalNavigator {
       return ExternalNavigatorImpl(context)
    }
    fun getSharingInteractor(context: Context): SharingInteractor {
        val navigator = getExternalNavigator(context)
        return SharingInteractorImpl(navigator)
    }


    private fun getSearchHistoryRepository(): SearchHistoryRepository = SearchHistoryRepositoryImpl(
        PrefsStorageClient<List<Track>>(
            getGson(),
            App.Companion.getInstance(),
            KEY_TRACK_HISTORY,
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    )

    fun getSearchHistoryInteractor(): SearchHistoryInteractor = SearchHistoryInteractorImpl(
        getSearchHistoryRepository())

    fun getGson(): Gson{
        return Gson()
    }
    private const val KEY_TRACK_HISTORY = "search_history"
}
}

 */