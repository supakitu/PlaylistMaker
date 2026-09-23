package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private var etSearchValue: Editable? = null

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
        val btnClear = findViewById<ImageButton>(R.id.btn_clear)
        val etSearch = findViewById<EditText>(R.id.et_search)

        toolbar.setNavigationOnClickListener { finish() }

        btnClear.setOnClickListener {
            etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(toolbar.windowToken, 0)
        }

        etSearch.requestFocus()
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Заглушка
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClear.visibility = btnClearVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                etSearchValue = s
            }
        })
    }

    private fun btnClearVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (etSearchValue != null) {
            val etSearch = findViewById<EditText>(R.id.et_search)
            val etSearchValue = savedInstanceState.getString("EDIT_TEXT_VALUE")

            etSearch.setText(etSearchValue)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (etSearchValue != null) {
            outState.putString("EDIT_TEXT_VALUE", etSearchValue.toString())
        }
    }
}