package com.example.cindernela

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.cindernela.R

class KolaseAdapter(private val fotoList: List<Foto>) :
    RecyclerView.Adapter<KolaseAdapter.FotoViewHolder>() {

    // 1. ViewHolder: Menyimpan referensi ke elemen tampilan (ImageView)
    class FotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewItem)
    }

    // 2. Membuat dan menginisiasi ViewHolder (mengatur layout item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_foto_kolase, parent, false)
        return FotoViewHolder(view)
    }

    // 3. Mengikat data (Foto) ke elemen tampilan
    override fun onBindViewHolder(holder: FotoViewHolder, position: Int) {
        val foto = fotoList[position]
        // Menetapkan resource ID gambar ke ImageView
        holder.imageView.setImageResource(foto.imageResId)
    }

    // 4. Mengembalikan jumlah item dalam daftar
    override fun getItemCount() = fotoList.size
}