package com.example.playlistmaker.search.data.clients

import com.example.playlistmaker.search.data.dto.Response

interface NetWorkClient {
    suspend fun doRequest(dto: Any): Response
}