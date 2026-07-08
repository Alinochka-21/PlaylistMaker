package com.example.playlistmaker.sharing.data.navigator

import android.content.Intent
import android.provider.ContactsContract
import com.example.playlistmaker.sharing.data.dto.EmailData

interface ExternalNavigator {
    fun shareLink(link: String): Intent
    fun openLink(link: String): Intent
    fun openEmail(emailData: EmailData): Intent
}

