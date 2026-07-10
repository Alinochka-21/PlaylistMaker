package com.example.playlistmaker.search.di.Domain

import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

 val interactorModule = module {

     single <TrackInteractor> {
         TracksInteractorImpl(get())
     }

     single <SearchHistoryInteractor> {
         SearchHistoryInteractorImpl (get())
     }
 }