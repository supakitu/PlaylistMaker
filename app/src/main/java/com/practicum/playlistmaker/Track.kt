package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    @SerializedName("artworkUrl100") val artworkUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
) {

    fun matches(track: Track): Boolean = trackId == track.trackId

    fun getArtworkUrl(ratio: Int) = artworkUrl.replaceAfterLast('/', "${ratio}x${ratio}bb.jpg")
}