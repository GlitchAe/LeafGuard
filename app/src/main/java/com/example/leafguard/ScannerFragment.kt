package com.example.leafguard

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScannerFragment : Fragment(R.layout.fragment_scanner) {

    // Variabel untuk CameraX
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File

    // Variabel untuk Lokasi
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Variabel untuk View
    private lateinit var previewView: PreviewView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.camera_preview)
        val shutterButton: ImageButton = view.findViewById(R.id.btn_shutter)
        val galleryButton: ImageButton = view.findViewById(R.id.btn_gallery)

        // Inisialisasi output file & executor
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Inisialisasi Fused Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Memulai kamera (setelah cek izin)
        startCamera()

        // Listener untuk tombol shutter
        shutterButton.setOnClickListener { takePhoto() }

        // Listener untuk tombol galeri (belum diimplementasi)
        galleryButton.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur galeri belum ada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        // Cek lagi apakah izin kamera ada
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
            return
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Atur Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Atur ImageCapture (untuk mengambil foto)
            imageCapture = ImageCapture.Builder().build()

            // Pilih kamera belakang
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                // Ikat use case ke lifecycle fragment
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("ScannerFragment", "Gagal binding use case", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Buat file untuk menyimpan foto
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Ambil foto
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("ScannerFragment", "Gagal ambil foto: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    // Setelah foto disimpan, dapatkan lokasi
                    getLastLocation(savedUri)
                }
            }
        )
    }

    @SuppressLint("MissingPermission") // Kita sudah cek izin di MainActivity
    private fun getLastLocation(imageUri: Uri) {
        // Cek lagi izin lokasi
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Izin lokasi tidak ada, lokasi diset default", Toast.LENGTH_SHORT).show()
            // Tetap lanjutkan tanpa lokasi
            navigateToHasil(imageUri, "Lokasi tidak ada")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val locationString: String
                if (location != null) {
                    locationString = "${location.latitude}, ${location.longitude}"
                } else {
                    locationString = "Lokasi tidak terdeteksi"
                }

                // Pindah ke halaman hasil dengan membawa URI gambar dan lokasi
                navigateToHasil(imageUri, locationString)
            }
            .addOnFailureListener {
                // Gagal dapat lokasi, tetap lanjutkan
                navigateToHasil(imageUri, "Gagal dapat lokasi")
            }
    }

    private fun navigateToHasil(imageUri: Uri, location: String) {
        // Kirim data ke HasilScanFragment
        val bundle = Bundle().apply {
            putString("imageUri", imageUri.toString())
            putString("location", location)
        }
        findNavController().navigate(R.id.action_scanner_to_hasil, bundle)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}