package com.example.leafguard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView // Import TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText: EditText = findViewById(R.id.et_email)
        val passwordEditText: EditText = findViewById(R.id.et_password)
        val loginButton: Button = findViewById(R.id.btn_login)

        // Temukan TextView baru
        val forgotPasswordTextView: TextView = findViewById(R.id.tv_forgot_password)
        val signUpTextView: TextView = findViewById(R.id.tv_sign_up)

        loginButton.setOnClickListener {
            // ... (logika login dummy tetap sama) ...
            if (emailEditText.text.toString() == "user@mail.com" && passwordEditText.text.toString() == "password") {
                val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("isLoggedIn", true)
                    putString("userEmail", emailEditText.text.toString())
                    apply()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tambahkan Listener untuk Lupa Password
        forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, "Fitur Lupa Password belum diimplementasikan.", Toast.LENGTH_SHORT).show()
            // Di sini nanti Anda bisa pindah ke Activity Lupa Password
        }

        // Tambahkan Listener untuk Sign Up
        signUpTextView.setOnClickListener {
            Toast.makeText(this, "Fitur Sign Up belum diimplementasikan.", Toast.LENGTH_SHORT).show()
            // Di sini nanti Anda bisa pindah ke Activity Sign Up
        }
    }
}