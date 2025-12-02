package com.example.cindernela

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlin.random.Random // Import untuk fungsi random

class UcapanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucapan)

        val bubuDuduView: ImageView = findViewById(R.id.imageViewBubuDudu)

        // Panggil fungsi untuk mendapatkan GIF yang dipilih secara acak
        val randomGifResId = getRandomBubuDuduGif()

        // Gunakan Glide untuk memuat GIF yang dipilih
        Glide.with(this)
            .asGif()
            .load(randomGifResId)
            .into(bubuDuduView)
    }

    /**
     * Fungsi untuk membuat daftar ID GIF dan memilih satu secara acak.
     */
    private fun getRandomBubuDuduGif(): Int {
        // 1. Definisikan daftar resource ID GIF Yang Mulia
        val gifResources = listOf(
            R.drawable.bd1,
            R.drawable.bd2,
            R.drawable.bd3,
            R.drawable.bd4,
            R.drawable.bd5,
        )

        // 2. Pilih indeks acak dari daftar tersebut
        val randomIndex = Random.nextInt(gifResources.size)

        // 3. Kembalikan ID resource GIF yang dipilih
        return gifResources[randomIndex]
    }
}