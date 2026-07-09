package com.example.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor

class MainViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private var darkTheme = MutableLiveData(settingsInteractor.getThemeMode())

    fun getLiveTheme(): LiveData<Boolean> = darkTheme

    fun getThemeMode(){
        darkTheme.value = settingsInteractor.getThemeMode()
    }

    companion object {

        fun getMainViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(Creator.getSettingsInteractor())
            }
        }
    }
}
