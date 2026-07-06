package com.example.playlistmaker.search.data.dto

class TracksSearchResponse(val searchString: String,
                           val searchType: String,
                           val results:List<TrackDto>): Response()