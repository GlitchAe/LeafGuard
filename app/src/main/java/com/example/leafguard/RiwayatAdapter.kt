package com.example.leafguard

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

// 1. Definisikan "jembatan" (listener) ke Fragment
interface RiwayatAdapterListener {
    fun onItemClicked(result: ScanResult)
    fun onDeleteClicked(result: ScanResult, position: Int)
}

class RiwayatAdapter(
    // Ubah ke MutableList agar bisa dihapus
    private val historyList: MutableList<ScanResult>,
    // Tambahkan listener sebagai parameter
    private val listener: RiwayatAdapterListener
) :
    RecyclerView.Adapter<RiwayatAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_history_image)
        val diagnosisView: TextView = itemView.findViewById(R.id.tv_history_diagnosis)
        val timestampView: TextView = itemView.findViewById(R.id.tv_history_timestamp)
        // Definisikan tombol & container
        val deleteButton: ImageButton = itemView.findViewById(R.id.btn_history_delete)
        val itemContainer: ConstraintLayout = itemView.findViewById(R.id.item_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]

        holder.diagnosisView.text = item.diagnosis
        holder.timestampView.text = item.timestamp
        holder.imageView.setImageURI(Uri.parse(item.imageUri))

        // 2. Hubungkan listener ke view
        holder.itemContainer.setOnClickListener {
            listener.onItemClicked(item)
        }
        holder.deleteButton.setOnClickListener {
            // holder.adapterPosition memastikan kita dapat posisi yang benar
            listener.onDeleteClicked(item, holder.adapterPosition)
        }
    }

    // 3. Buat fungsi untuk menghapus item (untuk animasi)
    fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
    }
}