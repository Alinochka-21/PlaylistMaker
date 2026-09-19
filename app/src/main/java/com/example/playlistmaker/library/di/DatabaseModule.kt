package com.example.playlistmaker.library.di

import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single{
        Room.databaseBuilder(androidContext(), AppDataBase::class.java,"database.db")
            .build()
    }
}