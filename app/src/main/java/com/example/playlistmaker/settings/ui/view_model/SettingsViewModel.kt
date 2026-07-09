package com.example.playlistmaker.settings.ui.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api_local.SettingsInteractor
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY


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

    companion object{
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(
                    Creator.getSharingInteractor(app),
                    Creator.getSettingsInteractor())
            }
        }
    }
}


