package com.example.playlistmaker.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceWork {
    private val baseTracksUrl = "https://itunes.apple.com"

    public val retrofit = Retrofit.Builder()
        .baseUrl(baseTracksUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    public val tracksService = retrofit.create(iTunesApi::class.java)
}