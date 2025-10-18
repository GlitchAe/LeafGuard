package com.example.leafguard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

// Setiap Activity harus berada di file terpisah.
class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2000 // Kurangi delay sedikit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Cek status login dari SharedPreferences
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false) // Default false

            val nextIntent = if (isLoggedIn) {
                // Jika sudah login, ke MainActivity
                Intent(this, MainActivity::class.java)
            } else {
                // Jika belum login, ke LoginActivity
                Intent(this, LoginActivity::class.java)
            }
            startActivity(nextIntent)
            finish()
        }, SPLASH_DELAY)
    }
}