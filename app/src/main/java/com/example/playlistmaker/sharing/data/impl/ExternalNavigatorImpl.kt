package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.dto.EmailData
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type ="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.practicum_website))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openLink() {
        val agreementIntent = Intent(
            Intent.ACTION_VIEW,
            context.getString(R.string.agreement_of_website).toUri()
        )
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(agreementIntent)
    }

    override fun openEmail() {
        val emailData = getSupportEmailData()
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, emailData.mail)
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.titleOfMail)
        supportIntent.putExtra(Intent.EXTRA_TEXT, emailData.content)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            mail = context.getString(R.string.mail),
            titleOfMail = context.getString(R.string.title_of_mail),
            content = context.getString(R.string.content_of_mail)
        )
    }
}
