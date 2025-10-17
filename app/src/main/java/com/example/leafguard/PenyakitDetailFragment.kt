package com.example.leafguard

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class PenyakitDetailFragment : Fragment(R.layout.fragment_penyakit_detail) {

    private var diagnosisName: String? = null
    private var imageUriString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ambil data yang dikirim dari RiwayatFragment
        arguments?.let {
            diagnosisName = it.getString("diagnosis")
            imageUriString = it.getString("imageUri")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivGambar: ImageView = view.findViewById(R.id.iv_detail_gambar)
        val tvDiagnosis: TextView = view.findViewById(R.id.tv_detail_diagnosis)
        val tvDeskripsi: TextView = view.findViewById(R.id.tv_detail_deskripsi)
        val tvPenanganan: TextView = view.findViewById(R.id.tv_detail_penanganan)

        // Set gambar & nama penyakit
        imageUriString?.let { ivGambar.setImageURI(Uri.parse(it)) }
        tvDiagnosis.text = diagnosisName

        // --- Database Penjelasan Penyakit (Dummy) ---
        when (diagnosisName) {
            "Penyakit Sigatoka" -> {
                tvDeskripsi.text = "Penyakit Sigatoka, disebabkan oleh jamur Mycosphaerella fijiensis, adalah salah satu penyakit daun pisang paling merusak di dunia. Gejalanya dimulai sebagai bintik-bintik kecil berwarna coklat muda pada daun, yang kemudian membesar menjadi goresan coklat tua atau hitam."
                tvPenanganan.text = "1. Sanitasi Kebun: Potong dan buang daun yang terinfeksi parah untuk mengurangi sumber spora.\n2. Drainase: Pastikan drainase kebun baik karena kelembapan tinggi memicu penyakit.\n3. Fungisida: Gunakan fungisida kontak atau sistemik sesuai anjuran.\n4. Pemupukan: Jaga kesehatan tanaman dengan pemupukan berimbang."
            }
            "Busuk Batang" -> { // Asumsi jika ada diagnosis lain
                tvDeskripsi.text = "Busuk batang (Penyakit Panama) disebabkan oleh jamur Fusarium oxysporum. Gejala awal meliputi menguningnya daun tua yang kemudian layu dan 'patah' di pangkal tangkai daun."
                tvPenanganan.text = "1. Karantina: Segera isolasi area yang terinfeksi.\n2. Eradikasi: Bongkar dan musnahkan pohon pisang yang terinfeksi.\n3. Varietas Resisten: Tanam varietas pisang yang tahan terhadap penyakit Panama."
            }
            "Sehat" -> {
                tvDeskripsi.text = "Daun tanaman Anda terlihat sehat. Tidak ada gejala penyakit yang terdeteksi."
                tvPenanganan.text = "1. Lanjutkan praktik perawatan yang baik.\n2. Lakukan pemantauan rutin untuk deteksi dini."
            }
            else -> {
                tvDeskripsi.text = "Diagnosis tidak dikenal. Informasi penyakit tidak tersedia."
                tvPenanganan.text = "Silakan hubungi ahli pertanian setempat untuk informasi lebih lanjut."
            }
        }
    }
}