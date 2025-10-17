package com.example.leafguard

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import java.util.Date // Pastikan ini di-import

// --- DATA CLASS UNTUK MENYIMPAN HASIL ---
// (Bisa diletakkan di file sendiri atau di sini)
data class ScanResult(
    val imageUri: String,
    val diagnosis: String,
    val location: String,
    val timestamp: String // Kita gunakan String agar sederhana
)

// --- "DATABASE" DUMMY KITA ---
// (Singleton object agar datanya tidak hilang saat pindah fragment)
object HistoryRepository {
    val historyList = mutableListOf<ScanResult>()
}
// -----------------------------------------


class HasilScanFragment : Fragment(R.layout.fragment_hasil_scan) {

    private var imageUriString: String? = null
    private var locationString: String? = null

    // Variabel penanda apakah sudah disimpan
    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ambil data dari arguments
        arguments?.let {
            imageUriString = it.getString("imageUri")
            locationString = it.getString("location")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpanButton: ImageButton = view.findViewById(R.id.btn_simpan)
        val hapusButton: ImageButton = view.findViewById(R.id.btn_hapus)
        val imageView: ImageView = view.findViewById(R.id.iv_hasil_gambar)
        val deskripsiView: TextView = view.findViewById(R.id.tv_hasil_deskripsi)
        // Ambil TextView diagnosis
        val diagnosisView: TextView = view.findViewById(R.id.tv_hasil_diagnosis)

        // Tampilkan gambar yang diambil
        imageUriString?.let {
            imageView.setImageURI(Uri.parse(it))
        }

        // Tampilkan lokasi
        deskripsiView.text = "Lokasi: $locationString"

        // --- Logika Tombol Simpan (CREATE) ---
        simpanButton.setOnClickListener {
            // Hanya jalankan jika BELUM disimpan
            if (!isSaved) {
                // 1. Buat objek data baru
                val newResult = ScanResult(
                    imageUri = imageUriString ?: "no_image",
                    diagnosis = diagnosisView.text.toString(), // Ambil diagnosis dari TextView
                    location = locationString ?: "Lokasi tidak diketahui",
                    timestamp = Date().toString() // Simpan waktu saat ini
                )

                // 2. Simpan ke database dummy kita
                HistoryRepository.historyList.add(newResult)

                // 3. Beri feedback ke pengguna
                Snackbar.make(view, "Hasil berhasil disimpan", Snackbar.LENGTH_SHORT).show()

                // 4. Matikan tombol & tandai sudah disimpan
                isSaved = true
                simpanButton.isEnabled = false
                simpanButton.alpha = 0.5f // Buat tombol jadi abu-abu
            }
        }

        // --- Logika Tombol Hapus (DELETE) ---
        hapusButton.setOnClickListener {
            // (Untuk sekarang, tombol hapus hanya kembali)
            // Nanti kita tambahkan logika delete dari database
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Hasil")
            .setMessage("Apakah Anda yakin ingin menghapus hasil scan ini?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Ya, Hapus") { dialog, which ->
                // Aksi jika pengguna menekan "Ya"
                Snackbar.make(requireView(), "Hasil berhasil dihapus", Snackbar.LENGTH_SHORT).show()

                // Kembali ke halaman sebelumnya (Scanner)
                findNavController().popBackStack()
            }
            .setNegativeButton("Batal") { dialog, which ->
                // Aksi jika pengguna menekan "Batal"
                dialog.dismiss()
            }
            .create()
            .show()
    }
}