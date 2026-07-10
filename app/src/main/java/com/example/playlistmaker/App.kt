package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.main.ui.di.mainViewModelModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.Domain.interactorModule
import com.example.playlistmaker.search.di.Domain.repositoryModule
import com.example.playlistmaker.search.di.searchDataModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.data.repositoryImpl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.di.domain.interactor.settingsInteractorModule
import com.example.playlistmaker.settings.di.domain.repository.settingsRepositoryModule
import com.example.playlistmaker.settings.di.viewModel.settingsViewModelModule
import com.example.playlistmaker.sharing.di.sharingInteractorModule
import com.example.playlistmaker.sharing.di.sharingDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val  KEY_DARK_THEME = "key_dark_theme"
class App : Application() {
    var darkTheme = false

    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                searchViewModelModule,
                repositoryModule,
                interactorModule,
                searchDataModule,
                playerViewModelModule,
                sharingDataModule,
                settingsRepositoryModule,
                settingsInteractorModule,
                sharingInteractorModule,
                settingsViewModelModule,
                mainViewModelModule
            )
        }
        instance = this
        val repository = SettingsRepositoryImpl(this)
        darkTheme = repository.getThemeMode()
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        if (darkThemeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    companion object{
        private lateinit var instance: App
        fun getInstance(): App = instance
    }
}