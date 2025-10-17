package com.example.leafguard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

// 1. Tambahkan implementasi RiwayatAdapterListener
class RiwayatFragment : Fragment(R.layout.fragment_riwayat), RiwayatAdapterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RiwayatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_history)
        setupAdapter() // Panggil fungsi setup
    }

    private fun setupAdapter() {
        // 2. Ambil data asli
        val data = HistoryRepository.historyList

        // 3. Kirim "this" (Fragment ini) sebagai listener
        // Kita kirim data yang sudah dibalik (agar terbaru di atas)
        adapter = RiwayatAdapter(data.asReversed().toMutableList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali kembali ke tab ini
        setupAdapter()
    }

    // --- FUNGSI DARI LISTENER YANG KITA BUAT ---

    // 4. Implementasi logika saat item diklik
    override fun onItemClicked(result: ScanResult) {
        // Siapkan data untuk dikirim
        val bundle = Bundle().apply {
            putString("diagnosis", result.diagnosis)
            putString("imageUri", result.imageUri)
        }
        // Pindah ke halaman detail (kita akan buat action-nya)
        findNavController().navigate(R.id.action_riwayat_to_detail, bundle)
    }

    // 5. Implementasi logika saat tombol hapus diklik
    override fun onDeleteClicked(result: ScanResult, position: Int) {
        // Tampilkan dialog konfirmasi
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Riwayat")
            .setMessage("Anda yakin ingin menghapus item ini: ${result.diagnosis}?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Ya, Hapus") { dialog, _ ->
                // 1. Hapus dari "database" (list asli)
                HistoryRepository.historyList.remove(result)

                // 2. Hapus dari adapter (list yang tampil)
                adapter.removeItem(position)

                Snackbar.make(requireView(), "Item dihapus", Snackbar.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}