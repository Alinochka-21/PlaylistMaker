package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var darkTheme = MutableLiveData(settingsInteractor.getThemeMode())

    fun getTheme(): LiveData<Boolean> = darkTheme

    fun changeTheme() {
        val new = !(darkTheme.value ?: false)
        darkTheme.postValue(new)
        settingsInteractor.setThemeMode(new)
    }

    fun share() {
        return sharingInteractor.shareApp()
    }
    fun support() {
        return sharingInteractor.openSupport()
    }
    fun agreement() {
        return sharingInteractor.openTerms()
    }
}


