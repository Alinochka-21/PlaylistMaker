package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.clients.NetWorkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val tracksService: iTunesApi): NetWorkClient {

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