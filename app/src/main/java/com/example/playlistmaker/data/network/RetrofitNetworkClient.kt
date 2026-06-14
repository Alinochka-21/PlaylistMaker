package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetWorkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(): NetWorkClient {
    private val baseTracksUrl = "https://itunes.apple.com"

    public val retrofit = Retrofit.Builder()
        .baseUrl(baseTracksUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    public val tracksService = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest){
            val response = tracksService.search(dto.searchString).execute()
            val body = response.body() ?: Response()
            body.resultCode = response.code()
            return body
        } else {
            return Response().apply{
                resultCode = 400
            }
        }
    }
}