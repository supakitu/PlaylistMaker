package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName = itemView.findViewById<TextView>(R.id.tv_track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.tv_artist_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.tv_track_time)
    private val artwork = itemView.findViewById<ImageView>(R.id.image_artwork)

    fun bind (model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .into(artwork)
    }
}