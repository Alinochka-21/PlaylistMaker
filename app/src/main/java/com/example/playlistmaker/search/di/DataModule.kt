package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.clients.NetWorkClient
import com.example.playlistmaker.search.data.clients.StorageClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.search.data.storage.PrefsStorageClient
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList


val searchDataModule = module {

    single <iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single <NetWorkClient> {
        RetrofitNetworkClient(get())
    }

    factory {
        Gson()
    }

    single {
        androidContext()
            .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    single <StorageClient<ArrayList<Track>>>{
        PrefsStorageClient (
            get(),
            get(),
            "search_history",
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    }

}