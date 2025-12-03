package com.example.cindernela

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.media.MediaPlayer
import com.bumptech.glide.Glide
import kotlin.random.Random

class UcapanActivity : AppCompatActivity() {

    // Deklarasi untuk MediaPlayer (Musik Latar)
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucapan)

        // Pastikan Yang Mulia menggunakan 1 dan 2 di sini:
        val bubuDuduView1: ImageView = findViewById(R.id.imageViewBubuDudu1)
        val bubuDuduView2: ImageView = findViewById(R.id.imageViewBubuDudu2)

        // Pilih GIF pertama secara acak
        val randomGifResId1 = getRandomBubuDuduGif()
        // Pilih GIF kedua secara acak (memungkinkan GIF yang sama, atau berbeda)
        val randomGifResId2 = getRandomBubuDuduGif()

        // Load GIF pertama
        Glide.with(this).asGif().load(randomGifResId1).into(bubuDuduView1)
        // Load GIF kedua
        Glide.with(this).asGif().load(randomGifResId2).into(bubuDuduView2)

        // --- 2. SETUP MUSIK LATAR ---
        try {
            // Inisiasi MediaPlayer dengan file dari res/raw/
            // Ganti R.raw.hbd_song sesuai dengan nama file .mp3 Yang Mulia!
            mediaPlayer = MediaPlayer.create(this, R.raw.hbd)
            mediaPlayer.isLooping = true // Ulangi musik terus menerus
            mediaPlayer.start() // Mulai putar musik
        } catch (e: Exception) {
            e.printStackTrace()
            // Jika file musik tidak ditemukan (misal R.raw.hbd_song tidak ada)
        }
    }

    /**
     * Fungsi untuk membuat daftar ID GIF (total 5) dan memilih satu secara acak.
     */
    private fun getRandomBubuDuduGif(): Int {
        // Daftar resource ID GIF Yang Mulia (sesuai dengan 5 file Yang Mulia: bd1 s/d bd5)
        val gifResources = listOf(
            R.drawable.bd1,
            R.drawable.bd2,
            R.drawable.bd3,
            R.drawable.bd4,
            R.drawable.bd5
        )

        // Pilih indeks acak (0 sampai 4)
        val randomIndex = Random.nextInt(gifResources.size)

        return gifResources[randomIndex]
    }

    // --- 3. KONTROL SIKLUS HIDUP MUSIK agar tidak error/boros baterai ---

    override fun onPause() {
        super.onPause()
        // Jeda (pause) musik saat Activity tidak terlihat
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // Lanjutkan musik saat Activity kembali terlihat
        if (::mediaPlayer.isInitialized && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Lepaskan sumber daya MediaPlayer saat Activity dihancurkan
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}