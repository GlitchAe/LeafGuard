package com.example.leafguard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

// Setiap Activity harus berada di file terpisah.
class SplashActivity : AppCompatActivity() {

    // Atur durasi splash screen (misalnya 3000ms = 3 detik)
    private val SPLASH_DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Hubungkan dengan layout XML-nya
        setContentView(R.layout.activity_splash)

        // Gunakan Handler untuk menunda perpindahan ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Buat Intent untuk pindah ke MainActivity
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)

            // Tutup SplashActivity agar tidak bisa kembali ke sini
            finish()
        }, SPLASH_DELAY)
    }
}