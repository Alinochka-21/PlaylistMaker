package com.example.playlistmaker.library.di

import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single{
        Room.databaseBuilder(androidContext(), AppDataBase::class.java,"database.db")
            .addMigrations(
                AppDataBase.MIGRATION_1_2,
                AppDataBase.MIGRATION_2_3,
                AppDataBase.MIGRATION_3_4,
                AppDataBase.MIGRATION_4_5
            )
            .build()
    }
}