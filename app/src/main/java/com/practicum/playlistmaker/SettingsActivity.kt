package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnUserAgreement = findViewById<MaterialButton>(R.id.btn_user_agreement)
        val btnSupport = findViewById<MaterialButton>(R.id.btn_support)
        val btnShare = findViewById<MaterialButton>(R.id.btn_share)

        toolbar.setNavigationOnClickListener { finish() }

        btnShare.setOnClickListener {
            val link = getString(R.string.link_practicum_android_developer)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            startActivity(intent)
        }

        btnSupport.setOnClickListener {
            val myEmail = getString(R.string.some_email)
            val emailSubject = getString(R.string.email_subject)
            val emailText = getString(R.string.email_text)

            val intent = Intent(Intent.ACTION_SENDTO, "mailto:$myEmail".toUri())
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            intent.putExtra(Intent.EXTRA_TEXT, emailText)
            startActivity(intent)
        }

        btnUserAgreement.setOnClickListener {
            val link = getString(R.string.link_practicum_offer)

            val intent = Intent(Intent.ACTION_VIEW, link.toUri())
            startActivity(intent)
        }
    }
}