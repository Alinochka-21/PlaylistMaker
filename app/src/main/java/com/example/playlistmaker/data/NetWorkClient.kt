package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response

interface NetWorkClient {
    fun doRequest(dto: Any): Response
}