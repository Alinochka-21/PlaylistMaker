package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator
import org.koin.dsl.module

val sharingDataModule = module {
    single <ExternalNavigator>{
        ExternalNavigatorImpl(get())
    }
}