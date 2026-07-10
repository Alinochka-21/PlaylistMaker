package com.example.playlistmaker.search.di.Domain

import com.example.playlistmaker.search.data.repository_impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository_impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory <TracksRepository> {
        TrackRepositoryImpl(get())
    }

    factory <SearchHistoryRepository > {
        SearchHistoryRepositoryImpl(get())
    }
}