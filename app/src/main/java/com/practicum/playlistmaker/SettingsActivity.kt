package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switchDarkTheme: SwitchMaterial
    private lateinit var btnShare: MaterialButton
    private lateinit var btnSupport: MaterialButton
    private lateinit var btnUserAgreement: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()

        toolbar.setNavigationOnClickListener { finish() }

        switchDarkTheme.isChecked = App.sharedPrefs.getBoolean(
            PrefsKeys.DARK_THEME.key,
            App.DARK_THEME_DEFAULT
        )
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            App.sharedPrefs.edit {
                putBoolean(PrefsKeys.DARK_THEME.key, isChecked)
            }

            App.switchTheme(isChecked)
        }

        btnShare.setOnClickListener {
            val link = getString(R.string.link_practicum_android_developer)

            val intent = Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, link)
            intent.type = "text/plain"

            startActivity(intent)
        }

        btnSupport.setOnClickListener {
            val myEmail = getString(R.string.some_email)
            val emailSubject = getString(R.string.email_subject)
            val emailText = getString(R.string.email_text)

            val intent = Intent(Intent.ACTION_SENDTO, "mailto:$myEmail".toUri())
                .putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                .putExtra(Intent.EXTRA_TEXT, emailText)

            startActivity(intent)
        }

        btnUserAgreement.setOnClickListener {
            val link = getString(R.string.link_practicum_offer)

            startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
        }
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        switchDarkTheme = findViewById(R.id.switch_dark_theme)
        btnShare = findViewById(R.id.btn_share)
        btnSupport = findViewById(R.id.btn_support)
        btnUserAgreement = findViewById(R.id.btn_user_agreement)
    }
}