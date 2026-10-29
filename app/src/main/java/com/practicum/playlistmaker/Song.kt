package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Song(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    @SerializedName("artworkUrl100") val artworkUrl: String,
)