package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.dto.EmailData
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(link: String): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type ="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT,link)
        return shareIntent
        context.startActivity(Intent.createChooser(shareIntent, "Shape APK"))
    }

    override fun openLink(link: String): Intent {
        val agreementIntent = Intent(
            Intent.ACTION_VIEW,
            link.toUri()
        )
        return agreementIntent
        context.startActivity(agreementIntent)
    }

    override fun openEmail(emailData: EmailData): Intent {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, emailData.mail)
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.titleOfMail)
        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.content)
        return supportIntent
        context.startActivity(supportIntent)
    }
}
