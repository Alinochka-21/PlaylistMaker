package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.clients.NetWorkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val tracksService: iTunesApi): NetWorkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                    val response = tracksService.search(dto.searchString)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            Response().apply {
                resultCode = 400
            }
        }
    }
}