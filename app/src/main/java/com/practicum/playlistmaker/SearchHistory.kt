package com.practicum.playlistmaker

import androidx.core.content.edit

class SearchHistory {

    companion object {
        val recentTracks = mutableListOf<Track>()
        var buffer: Track? = null
            set(value) {
                field = value

                if (value != null) {
                    put(value)
                }
            }

        fun fetchData() {
            val tracksJson = App.sharedPrefs.getString(PrefsKeys.RECENT_TRACKS.key, null)

            if (tracksJson != null) {
                val tracks = App.gson.fromJson(tracksJson, Array<Track>::class.java)
                recentTracks.clear()
                recentTracks.addAll(tracks)
            }
        }

        private fun put(track: Track) {
            var match: Track? = null

            if (recentTracks.isNotEmpty()) {
                for (item in recentTracks) {
                    if (item.matches(track)) {
                        match = track
                    }
                }

                if (match != null) {
                    recentTracks.remove(match)
                    recentTracks.add(0, track)
                } else {
                    if (recentTracks.size < 10) {
                        recentTracks.add(0, track)
                    } else {
                        recentTracks.removeAt(9)
                        recentTracks.add(0, track)
                    }
                }
            } else {
                recentTracks.add(0, track)
            }

            App.sharedPrefs.edit {
                putString(PrefsKeys.RECENT_TRACKS.key, App.gson.toJson(recentTracks))
            }
        }
    }
}