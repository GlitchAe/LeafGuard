package com.example.leafguard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // 1. Daftar Izin yang kita perlukan
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // 2. Buat "peluncur" untuk meminta izin
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Cek apakah semua izin diberikan setelah user memilih
            var allGranted = true
            permissions.entries.forEach {
                if (!it.value) allGranted = false
            }

            if (allGranted) {
                // Izin diberikan, tidak perlu lakukan apa-apa
                Toast.makeText(this, "Izin diberikan!", Toast.LENGTH_SHORT).show()
            } else {
                // Izin ditolak, beri tahu user
                Toast.makeText(this, "Aplikasi mungkin tidak berfungsi dengan baik tanpa izin.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        // 3. Cek dan minta izin saat Activity dibuat
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    // 4. Fungsi helper untuk mengecek apakah semua izin sudah ada
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}