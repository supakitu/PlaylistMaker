package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Song(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    @SerializedName("artworkUrl") val artworkUrl: String,
)