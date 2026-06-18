package com.example.playlistmaker.data.dto

class TracksSearchResponse(val searchString: String,
                           val searchType: String,
                           val results:List<TrackDto>): Response()