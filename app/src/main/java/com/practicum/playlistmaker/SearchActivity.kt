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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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

    private val tracks = mutableListOf<Track>()
    private var lastSearchQuery: String? = null

    private lateinit var rvTracks: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnUpdate: Button
    private lateinit var placeholderNothingFound: TextView
    private lateinit var placeholderNoInternet: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvRecentTracks: TextView
    private lateinit var rvRecentTracks: RecyclerView
    private lateinit var btnClearHistory: Button

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
        val btnClearText = findViewById<ImageButton>(R.id.btn_clear_text)
        rvTracks = findViewById(R.id.rv_tracks)
        etSearch = findViewById(R.id.et_search)
        btnUpdate = findViewById(R.id.btn_update)
        placeholderNothingFound = findViewById(R.id.placeholder_nothing_found)
        placeholderNoInternet = findViewById(R.id.placeholder_no_internet)
        progressBar = findViewById(R.id.progress_bar)
        tvRecentTracks = findViewById(R.id.tv_recent_tracks)
        rvRecentTracks = findViewById(R.id.rv_recent_tracks)
        btnClearHistory = findViewById(R.id.btn_clear_history)

        toolbar.setNavigationOnClickListener { finish() }

        btnClearText.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(toolbar.windowToken, 0)
        }

        SearchHistory.fetchData()
        rvRecentTracks.adapter = RecentTracksAdapter(SearchHistory.recentTracks)

        rvTracks.adapter = TracksAdapter(tracks)

        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (SearchHistory.recentTracks.isNotEmpty() && hasFocus && etSearch.text.isEmpty()) {
                hideRvTracks()
                showRecentTracks()
            } else {
                hideRecentTracks()
            }
        }
        etSearch.requestFocus()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearText.isVisible = !s.isNullOrEmpty()

                if (SearchHistory.recentTracks.isNotEmpty() && etSearch.hasFocus() && s.isNullOrEmpty()) {
                    hideRvTracks()
                    showRecentTracks()
                } else {
                    hideRecentTracks()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.isNotEmpty()) {
                    search(etSearch.text.toString())
                    lastSearchQuery = etSearch.text.toString()

                    hideRvTracks()
                    hideEmptyState()
                    hideOfflineState()

                    showProgressBar()
                }

                true
            } else {
                false
            }
        }

        btnUpdate.setOnClickListener {
            if (lastSearchQuery != null) {
                search(lastSearchQuery!!)

                showProgressBar()
                hideOfflineState()
            }
        }

        btnClearHistory.setOnClickListener {
            App.sharedPrefs.edit {
                remove(PrefsKeys.RECENT_TRACKS.key)
            }

            SearchHistory.recentTracks.clear()

            hideRecentTracks()
        }
    }

    private fun search(text: String) {
        val callback = object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                hideProgressBar()
                hideRecentTracks()

                if (response.body() != null) {
                    val responseBody = response.body()!!

                    if (responseBody.results.isNotEmpty()) {
                        tracks.clear()
                        tracks.addAll(responseBody.results)

                        rvTracks.adapter?.notifyDataSetChanged()
                        showRvTracks()
                    } else {
                        showEmptyState()
                    }
                } else {
                    showEmptyState()
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                hideProgressBar()
                showOfflineState()
            }
        }

        iTunesApiService.search(text).enqueue(callback)
    }

    private fun showRvTracks() {
        rvTracks.scrollToPosition(0)
        rvTracks.isVisible = true
    }

    private fun hideRvTracks() {
        rvTracks.isVisible = false
    }

    private fun showProgressBar() {
        progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun showOfflineState() {
        placeholderNoInternet.isVisible = true
        btnUpdate.isVisible = true
    }

    private fun hideOfflineState() {
        placeholderNoInternet.isVisible = false
        btnUpdate.isVisible = false
    }

    private fun showEmptyState() {
        placeholderNothingFound.isVisible = true
    }

    private fun hideEmptyState() {
        placeholderNothingFound.isVisible = false
    }

    private fun showRecentTracks() {
        hideEmptyState()
        hideOfflineState()
        hideRecentTracks()

        rvRecentTracks.adapter?.notifyDataSetChanged()

        tvRecentTracks.isVisible = true
        rvRecentTracks.isVisible = true
        btnClearHistory.isVisible = true
    }

    private fun hideRecentTracks() {
        tvRecentTracks.isVisible = false
        rvRecentTracks.isVisible = false
        btnClearHistory.isVisible = false
    }
}