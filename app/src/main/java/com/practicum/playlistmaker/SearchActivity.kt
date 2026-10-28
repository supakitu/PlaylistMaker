package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApiService = retrofit.create(ITunesApi::class.java)

    private var songs = ArrayList<Song>()
    private var lastSearchQuery: String? = null

    private lateinit var etSearch: EditText
    private lateinit var rvTracks: RecyclerView
    private lateinit var imageNoInternet: ImageView
    private lateinit var imageNothingFound: ImageView
    private lateinit var tvNoInternet: TextView
    private lateinit var tvNothingFound: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnClear = findViewById<ImageButton>(R.id.btn_clear_text)
        etSearch = findViewById(R.id.et_search)
        rvTracks = findViewById(R.id.rv_songs)
        imageNoInternet = findViewById(R.id.image_placeholder_no_internet)
        imageNothingFound = findViewById(R.id.image_placeholder_nothing_found)
        tvNoInternet = findViewById(R.id.tv_placeholder_no_internet)
        tvNothingFound = findViewById(R.id.tv_placeholder_nothing_found)
        progressBar = findViewById(R.id.progress_bar)
        btnUpdate = findViewById(R.id.btn_update)

        toolbar.setNavigationOnClickListener { finish() }

        btnClear.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(toolbar.windowToken, 0)
        }

        rvTracks.adapter = SongsAdapter(songs)

        etSearch.requestFocus()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.isNotEmpty()) {
                    search(etSearch.text.toString())
                    lastSearchQuery = etSearch.text.toString()

                    imageNoInternet.visibility = View.GONE
                    tvNoInternet.visibility = View.GONE

                    imageNothingFound.visibility = View.GONE
                    tvNothingFound.visibility = View.GONE
                    btnUpdate.visibility = View.GONE

                    progressBar.visibility = View.VISIBLE
                }

                true
            }
            false
        }

        btnUpdate.setOnClickListener {
            if (lastSearchQuery != null) {
                search(lastSearchQuery!!)

                imageNoInternet.visibility = View.GONE
                tvNoInternet.visibility = View.GONE

                imageNothingFound.visibility = View.GONE
                tvNothingFound.visibility = View.GONE
                btnUpdate.visibility = View.GONE

                progressBar.visibility = View.VISIBLE
            }
        }
    }

    fun search(text: String) {
        iTunesApiService.search(text).enqueue(object : Callback<SongsResponse> {
            override fun onResponse(call: Call<SongsResponse>, response: Response<SongsResponse>) {
                progressBar.visibility = View.GONE

                if (response.body()?.results?.isNotEmpty() == true) {
                    songs.clear()
                    songs.addAll(response.body()?.results!!)

                    rvTracks.adapter?.notifyDataSetChanged()
                } else {
                    songs.clear()
                    rvTracks.adapter?.notifyDataSetChanged()

                    imageNothingFound.visibility = View.VISIBLE
                    tvNothingFound.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                progressBar.visibility = View.GONE

                songs.clear()
                rvTracks.adapter?.notifyDataSetChanged()

                imageNoInternet.visibility = View.VISIBLE
                tvNoInternet.visibility = View.VISIBLE
                btnUpdate.visibility = View.VISIBLE
            }
        })
    }
}