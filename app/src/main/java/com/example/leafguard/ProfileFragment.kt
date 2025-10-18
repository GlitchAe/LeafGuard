package com.example.leafguard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileImageView: CircleImageView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var logoutButton: Button

    // Launcher untuk memilih gambar dari galeri
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageView.setImageURI(it)
            // Simpan URI gambar ke SharedPreferences
            saveProfileImageUri(it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.iv_profile_picture)
        emailTextView = view.findViewById(R.id.tv_profile_email)
        phoneTextView = view.findViewById(R.id.tv_profile_phone) // Kita masih pakai dummy text
        logoutButton = view.findViewById(R.id.btn_logout)

        // Muat data profil saat fragment dibuat
        loadProfileData()

        // Listener untuk ganti gambar
        profileImageView.setOnClickListener {
            pickImageLauncher.launch("image/*") // Buka galeri untuk memilih gambar
        }

        // Listener untuk logout
        logoutButton.setOnClickListener {
            // Hapus status login
            val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", false)
                remove("userEmail")
                // Hapus juga URI gambar jika perlu
                // remove("profileImageUri")
                apply()
            }

            // Kembali ke LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            // Flag agar tidak bisa kembali ke MainActivity setelah logout
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadProfileData() {
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("userEmail", "Email tidak ditemukan")
        val imageUriString = sharedPref.getString("profileImageUri", null)

        emailTextView.text = email
        imageUriString?.let {
            profileImageView.setImageURI(Uri.parse(it))
        }
    }

    private fun saveProfileImageUri(uriString: String) {
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profileImageUri", uriString)
            apply()
        }
    }
}