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

    private var tracks = ArrayList<Track>()
    private var lastSearchQuery: String? = null

    private lateinit var etSearch: EditText
    private lateinit var rvTracks: RecyclerView
    private lateinit var placeholderNothingFound: TextView
    private lateinit var placeholderNoInternet: TextView
    private lateinit var btnUpdate: Button
    private lateinit var progressBar: ProgressBar

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
        etSearch = findViewById(R.id.et_search)
        rvTracks = findViewById(R.id.rv_tracks)
        placeholderNothingFound = findViewById(R.id.placeholder_nothing_found)
        placeholderNoInternet = findViewById(R.id.placeholder_no_internet)
        btnUpdate = findViewById(R.id.btn_update)
        progressBar = findViewById(R.id.progress_bar)

        toolbar.setNavigationOnClickListener { finish() }

        btnClearText.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(toolbar.windowToken, 0)
        }

        rvTracks.adapter = TracksAdapter(tracks)

        etSearch.requestFocus()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearText.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.isNotEmpty()) {
                    search(etSearch.text.toString())
                    lastSearchQuery = etSearch.text.toString()

                    hideRecycler()
                    showProgressBar()

                    hidePlaceholderNothingFound()
                    hidePlaceholderNoInternet()
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
                hidePlaceholderNoInternet()
            }
        }
    }

    private fun search(text: String) {
        val callback = object : Callback<TracksResponse> {
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                hideProgressBar()

                if (response.body() != null) {
                    val responseBody = response.body()!!

                    if (responseBody.results.isNotEmpty()) {
                        tracks.clear()
                        tracks.addAll(responseBody.results)

                        rvTracks.adapter?.notifyDataSetChanged()
                        showRecycler()
                    } else {
                        showPlaceholderNothingFound()
                    }
                } else {
                    showPlaceholderNothingFound()
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                hideProgressBar()
                showPlaceholderNoInternet()
            }
        }

        iTunesApiService.search(text).enqueue(callback)
    }

    private fun showRecycler() {
        rvTracks.scrollToPosition(0)
        rvTracks.visibility = View.VISIBLE
    }

    private fun hideRecycler() {
        rvTracks.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showPlaceholderNoInternet() {
        placeholderNoInternet.visibility = View.VISIBLE
        btnUpdate.visibility = View.VISIBLE
    }

    private fun hidePlaceholderNoInternet() {
        btnUpdate.visibility = View.GONE
        placeholderNoInternet.visibility = View.GONE
    }

    private fun showPlaceholderNothingFound() {
        placeholderNothingFound.visibility = View.VISIBLE
    }

    private fun hidePlaceholderNothingFound() {
        placeholderNothingFound.visibility = View.GONE
    }
}