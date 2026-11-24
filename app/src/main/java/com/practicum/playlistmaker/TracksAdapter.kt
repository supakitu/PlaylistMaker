package com.practicum.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_track, parent, false)

        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            SearchHistory.put(tracks[position])

            val trackJson = App.gson.toJson(tracks[position])
            val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
                .putExtra("TRACK", trackJson)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tracks.size
}