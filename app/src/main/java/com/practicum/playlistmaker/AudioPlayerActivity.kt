package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var imageArtwork: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvTrackTimeRemaining: TextView

    private lateinit var tvCollectionNameColumnLeft: TextView
    private lateinit var tvReleaseDateColumnLeft: TextView

    private lateinit var tvTrackTimeColumnRight: TextView
    private lateinit var tvCollectionNameColumnRight: TextView
    private lateinit var tvReleaseDateColumnRight: TextView
    private lateinit var tvPrimaryGenreNameColumnRight: TextView
    private lateinit var tvCountryColumnRight: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        imageArtwork = findViewById(R.id.image_artwork)
        tvTrackName = findViewById(R.id.tv_track_name)
        tvArtistName = findViewById(R.id.tv_artist_name)
        val btnAddToMediaLibrary = findViewById<Button>(R.id.btn_add_to_media_library)
        val btnPlay = findViewById<Button>(R.id.btn_play)
        val btnAddToFavorites = findViewById<Button>(R.id.btn_add_to_favorites)
        tvTrackTimeRemaining = findViewById(R.id.tv_track_time_remaining)

        tvCollectionNameColumnLeft = findViewById(R.id.tv_collection_name_column_left)
        tvReleaseDateColumnLeft = findViewById(R.id.tv_release_date_column_left)

        tvTrackTimeColumnRight = findViewById(R.id.tv_track_time_column_right)
        tvCollectionNameColumnRight = findViewById(R.id.tv_collection_name_column_right)
        tvReleaseDateColumnRight = findViewById(R.id.tv_release_date_column_right)
        tvPrimaryGenreNameColumnRight = findViewById(R.id.tv_primary_genre_name_column_right)
        tvCountryColumnRight = findViewById(R.id.tv_country_column_right)

        loadFromIntent()
    }

    private fun loadFromIntent() {
        val trackJson = intent.getStringExtra("TRACK")

        if (trackJson != null) {
            val track = App.gson.fromJson(trackJson, Track::class.java)

            Glide.with(this)
                .load(track.getArtworkUrl(512))
                .placeholder(R.drawable.placeholder_track)
                .transform(RoundedCorners(8f.toPx(this)))
                .into(imageArtwork)

            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackTimeRemaining.text = track.trackTime.toMmSs()

            tvTrackTimeColumnRight.text = track.trackTime.toMmSs()

            if (track.collectionName.isNotEmpty()) {
                tvCollectionNameColumnRight.text = track.collectionName

                tvCollectionNameColumnLeft.isVisible = true
                tvCollectionNameColumnRight.isVisible = true
            }

            if (track.releaseDate.isNotEmpty()) {
                tvReleaseDateColumnRight.text = track.releaseDate.take(4)

                tvReleaseDateColumnLeft.isVisible = true
                tvReleaseDateColumnRight.isVisible = true
            }

            tvPrimaryGenreNameColumnRight.text = track.primaryGenreName
            tvCountryColumnRight.text = track.country
        }
    }
}