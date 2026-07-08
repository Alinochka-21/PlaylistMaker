package com.example.playlistmaker.sharing.domain.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.dto.EmailData
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor{

    override fun shareApp(): Intent {
        return externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms(): Intent {
        return externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport(): Intent {
        return externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.practicum_website)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            mail = context.getString(R.string.mail),
            titleOfMail = context.getString(R.string.title_of_mail),
            content = context.getString(R.string.content_of_mail)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement_of_website)
    }
}