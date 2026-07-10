package com.example.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor

class MainViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private var darkTheme = MutableLiveData(settingsInteractor.getThemeMode())

    fun getLiveTheme(): LiveData<Boolean> = darkTheme

    fun getThemeMode(){
        darkTheme.value = settingsInteractor.getThemeMode()
    }
}
