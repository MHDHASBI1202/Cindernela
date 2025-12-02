package com.example.cindernela

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cindernela.ui.theme.CindernelaTheme

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cindernela.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Siapkan Daftar Data (70 Foto)
        val fotoList = prepareFotoList()

        // 2. Inisiasi RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewKolase)

        // 3. Atur Layout Manager (Grid 3 kolom untuk kolase)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager

        // 4. Atur Adapter
        val adapter = KolaseAdapter(fotoList)
        recyclerView.adapter = adapter

        val fabUcapan: FloatingActionButton = findViewById(R.id.fabShowUcapan)
        fabUcapan.setOnClickListener {
            // Membuat Intent untuk berpindah Activity
            val intent = Intent(this, UcapanActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk membuat daftar 70 foto
    private fun prepareFotoList(): List<Foto> {
        val list = mutableListOf<Foto>()

        for (i in 1..70) {
            // Membentuk nama resource file: "foto_01", "foto_02", ..., "foto_70"
            val resourceName = String.format("yay%02d", i)

            // Mendapatkan Resource ID dari nama file di folder /res/drawable
            val resourceId = resources.getIdentifier(
                resourceName,
                "drawable",
                packageName
            )

            if (resourceId != 0) {
                list.add(Foto(resourceId))
            } else {

            }
        }

        return list
    }
}