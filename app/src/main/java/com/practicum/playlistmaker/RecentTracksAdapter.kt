package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView

class RecentTracksAdapter(private val recentTracks: List<Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(recentTracks[position])
    }

    override fun getItemCount(): Int = recentTracks.size
}