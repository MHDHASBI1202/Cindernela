package com.example.cindernela

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cindernela.ui.theme.CindernelaTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalResources

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi dan mulai putar musik
        mediaPlayer = MediaPlayer.create(this, R.raw.hbd)
        mediaPlayer?.isLooping = true // Putar musik secara looping
        mediaPlayer?.start()

        setContent {
            CindernelaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BirthdayScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan dan bebaskan sumber daya MediaPlayer saat Activity dihancurkan
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}

// 1. Definisikan daftar gambar dan ucapan
val imageIds = listOf(
    R.drawable.yay1, R.drawable.yay2, R.drawable.yay3, R.drawable.yay4, R.drawable.yay5,
    R.drawable.yay6, R.drawable.yay7, R.drawable.yay8, R.drawable.yay9, R.drawable.yay10,
    R.drawable.yay11, R.drawable.yay12, R.drawable.yay13, R.drawable.yay14, R.drawable.yay15,
    R.drawable.yay16, R.drawable.yay17, R.drawable.yay18, R.drawable.yay19, R.drawable.yay20,
    R.drawable.yay21, R.drawable.yay22, R.drawable.yay23, R.drawable.yay24, R.drawable.yay25,
    R.drawable.yay26, R.drawable.yay27, R.drawable.yay28, R.drawable.yay29, R.drawable.yay30,
    R.drawable.yay31, R.drawable.yay32, R.drawable.yay33, R.drawable.yay34, R.drawable.yay35,
    R.drawable.yay36, R.drawable.yay37, R.drawable.yay38, R.drawable.yay39, R.drawable.yay40,
    R.drawable.yay41, R.drawable.yay42, R.drawable.yay43, R.drawable.yay44, R.drawable.yay45,
    R.drawable.yay46, R.drawable.yay47, R.drawable.yay48, R.drawable.yay49, R.drawable.yay50,
    R.drawable.yay51, R.drawable.yay52, R.drawable.yay53, R.drawable.yay54, R.drawable.yay55,
    R.drawable.yay56, R.drawable.yay57, R.drawable.yay58, R.drawable.yay59, R.drawable.yay60,
    R.drawable.yay61, R.drawable.yay62, R.drawable.yay63, R.drawable.yay64, R.drawable.yay65,
    R.drawable.yay66, R.drawable.yay67, R.drawable.yay68, R.drawable.yay69, R.drawable.yay70,
    // Ulangi daftar untuk memastikan scroll looping terlihat mulus (70 gambar lagi)
    R.drawable.yay1, R.drawable.yay2, R.drawable.yay3, R.drawable.yay4, R.drawable.yay5,
    R.drawable.yay6, R.drawable.yay7, R.drawable.yay8, R.drawable.yay9, R.drawable.yay10,
    R.drawable.yay11, R.drawable.yay12, R.drawable.yay13, R.drawable.yay14, R.drawable.yay15,
    R.drawable.yay16, R.drawable.yay17, R.drawable.yay18, R.drawable.yay19, R.drawable.yay20,
    R.drawable.yay21, R.drawable.yay22, R.drawable.yay23, R.drawable.yay24, R.drawable.yay25,
    R.drawable.yay26, R.drawable.yay27, R.drawable.yay28, R.drawable.yay29, R.drawable.yay30,
    R.drawable.yay31, R.drawable.yay32, R.drawable.yay33, R.drawable.yay34, R.drawable.yay35,
    R.drawable.yay36, R.drawable.yay37, R.drawable.yay38, R.drawable.yay39, R.drawable.yay40,
    R.drawable.yay41, R.drawable.yay42, R.drawable.yay43, R.drawable.yay44, R.drawable.yay45,
    R.drawable.yay46, R.drawable.yay47, R.drawable.yay48, R.drawable.yay49, R.drawable.yay50,
    R.drawable.yay51, R.drawable.yay52, R.drawable.yay53, R.drawable.yay54, R.drawable.yay55,
    R.drawable.yay56, R.drawable.yay57, R.drawable.yay58, R.drawable.yay59, R.drawable.yay60,
    R.drawable.yay61, R.drawable.yay62, R.drawable.yay63, R.drawable.yay64, R.drawable.yay65,
    R.drawable.yay66, R.drawable.yay67, R.drawable.yay68, R.drawable.yay69, R.drawable.yay70,
)

val birthdayMessages = listOf(
    "Selamat ulang tahun, Sayang! Kaulah hadiah terindah dalam hidupku. 💖",
    "Semoga harimu dipenuhi tawa dan kebahagiaan. Aku mencintaimu! ✨",
    "Untuk ratu di hatiku, semoga semua impianmu tercapai. 👑",
    "Setiap tahun bersamamu adalah sebuah petualangan. Happy Birthday! 😊",
    "Terima kasih sudah menjadi dirimu. Selamat ulang tahun, cintaku. ❤️"
)

// 2. Composable untuk layar utama
@Composable
fun BirthdayScreen(modifier: Modifier = Modifier) {
    // State untuk mengelola index pesan yang sedang ditampilkan
    var currentMessageIndex by remember { mutableIntStateOf(0) }
    val message = birthdayMessages[currentMessageIndex]

    // State dan CoroutineScope untuk auto-scroll
    val listState = rememberLazyListState()
    val totalUniqueImages = imageIds.size / 2 // Jumlah gambar unik (misalnya 70)

    // Logika Auto-Scroll Looping
    LaunchedEffect(Unit) {
        // 1. Scroll cepat ke posisi awal (di tengah daftar yang digandakan)
        // Ini memungkinkan scrolling ke bawah dan juga ke atas jika diperlukan
        listState.scrollToItem(totalUniqueImages)

        // 2. Auto-Scroll ke bawah secara perlahan
        while(true) {
            val nextIndex = listState.firstVisibleItemIndex + 1

            // Cek apakah sudah mendekati akhir dari duplikasi pertama (misalnya 5 item terakhir)
            // Jika sudah, "teleport" kembali ke bagian tengah list untuk looping tanpa terlihat berkedip
            if (listState.firstVisibleItemIndex >= imageIds.size - 5) {
                listState.scrollToItem(totalUniqueImages)
                continue // Lanjutkan ke iterasi berikutnya
            }

            // 3. Animasi scroll ke item berikutnya (lebih lambat)
            listState.animateScrollToItem(
                index = nextIndex,
                scrollOffset = 0
            )

            // 4. Menggunakan delay untuk mengontrol kecepatan scroll (1.5 detik per gambar)
            delay(1500)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // 3. Tambahkan clickable untuk mengganti teks
            .clickable {
                // Ganti pesan ke pesan berikutnya (looping)
                currentMessageIndex = (currentMessageIndex + 1) % birthdayMessages.size
            }
    ) {
        // Lapisan 1: Scrolling Gambar
        ImageScrollBackground(listState)

        // Lapisan 2: Overlay Teks (Semi Transparan dan Ganti Teks)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                // Latar belakang semi-transparan
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f), // Hitam 50% transparan
                        MaterialTheme.shapes.medium
                    )
                    .padding(24.dp),
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Composable untuk menampilkan list gambar yang bisa di-scroll
@Composable
fun ImageScrollBackground(listState: LazyListState) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(imageIds.size) { index ->
            Image(
                painter = painterResource(id = imageIds[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    // Memastikan gambar memenuhi tinggi layar agar scrolling terlihat jelas
                    .height(LocalResources.current.displayMetrics.heightPixels.dp / LocalResources.current.displayMetrics.density)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayScreenPreview() {
    CindernelaTheme {
        BirthdayScreen()
    }
}