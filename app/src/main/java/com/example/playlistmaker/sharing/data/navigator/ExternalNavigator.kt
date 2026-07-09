package com.example.playlistmaker.sharing.data.navigator

import android.content.Intent
import android.provider.ContactsContract
import com.example.playlistmaker.sharing.data.dto.EmailData

interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
}

