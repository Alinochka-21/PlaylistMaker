package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor{

    override fun shareApp() {
        return externalNavigator.shareLink()
    }

    override fun openTerms() {
        return externalNavigator.openLink()
    }

    override fun openSupport() {
        return externalNavigator.openEmail()
    }
}