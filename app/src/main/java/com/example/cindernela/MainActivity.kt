package com.example.cindernela

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cindernela.ui.theme.CindernelaTheme
import com.example.cindernela.ui.theme.PastelPinkPrimary // Impor warna pink pastel
import kotlinx.coroutines.delay
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest


class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hentikan dan bebaskan pemutar media sebelumnya
        mediaPlayer?.stop()
        mediaPlayer?.release()

        // PERBAIKAN MUSIK: Gunakan AudioAttributes untuk volume yang benar
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        mediaPlayer = MediaPlayer.create(this, R.raw.hbd)
        mediaPlayer?.setAudioAttributes(audioAttributes)
        mediaPlayer?.isLooping = true // Memastikan musik berulang

        // Atur volume ke MAKSIMUM (1.0f)
        mediaPlayer?.setVolume(1.0f, 1.0f)

        mediaPlayer?.start()

        setContent {
            // State untuk mengelola perpindahan layar (Welcome Screen vs Birthday Screen)
            var showWelcomeScreen by remember { mutableStateOf(true) }

            CindernelaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showWelcomeScreen) {
                        // Tampilkan Welcome Screen
                        WelcomeScreen(
                            onTapToContinue = { showWelcomeScreen = false },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        // Tampilkan Birthday Screen
                        BirthdayScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
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

// =========================================================================================
// 3. COMPOSABLE BARU: Welcome Screen
// =========================================================================================
@Composable
fun WelcomeScreen(onTapToContinue: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            // Gunakan warna Pink Pastel sebagai latar belakang
            .background(color = PastelPinkPrimary)
            .clickable(onClick = onTapToContinue), // Tombol 'tap' untuk melanjutkan
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Aloo, selamat datang yay",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "tekan kalau kepo",
                fontSize = 20.sp,
                // Beri sedikit transparansi agar terlihat elegan
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
// =========================================================================================


// Data gambar dan ucapan
val imageIds = listOf(
    // ... (Daftar imageIds yang sangat panjang)
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

val birthdayItems = listOf(
    Pair("Selamat ulang tahun, Sayang! Kaulah hadiah terindah dalam hidupku. 💖", R.drawable.bd5),
    Pair("Semoga harimu dipenuhi tawa dan kebahagiaan. Aku mencintaimu! ✨", R.drawable.bd4),
    Pair("Untuk ratu di hatiku, semoga semua impianmu tercapai. 👑", R.drawable.bd1),
    Pair("Setiap tahun bersamamu adalah sebuah petualangan. Happy Birthday! 😊", R.drawable.bd2),
    Pair("Terima kasih sudah menjadi dirimu. Selamat ulang tahun, cintaku. ❤️", R.drawable.bd3)
)

// Composable untuk menampilkan GIF menggunakan Coil
@Composable
fun GifImage(
    gifResId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(gifResId)
        .decoderFactory(ImageDecoderDecoder.Factory())
        .build()

    val painter = rememberAsyncImagePainter(imageRequest)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}


// Composable untuk layar utama (Birthday Screen)
@Composable
fun BirthdayScreen(modifier: Modifier = Modifier) {
    // State untuk mengelola index pesan yang sedang ditampilkan
    var currentMessageIndex by remember { mutableStateOf(0) }
    val currentItem = birthdayItems[currentMessageIndex]
    val message = currentItem.first
    val gifResId = currentItem.second

    // State untuk auto-scroll
    val listState = rememberLazyListState()
    val totalUniqueImages = imageIds.size / 2

    // Logika Auto-Scroll Looping
    LaunchedEffect(Unit) {
        listState.scrollToItem(totalUniqueImages)

        while(true) {
            val nextIndex = listState.firstVisibleItemIndex + 1

            if (listState.firstVisibleItemIndex >= imageIds.size - 5) {
                listState.scrollToItem(totalUniqueImages)
            } else {
                listState.animateScrollToItem(
                    index = nextIndex,
                    scrollOffset = 0
                )
            }

            delay(1500)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Tambahkan clickable untuk mengganti teks
            .clickable {
                // Ganti pesan ke pesan berikutnya (looping)
                currentMessageIndex = (currentMessageIndex + 1) % birthdayItems.size
            }
    ) {
        // Lapisan 1: Scrolling Gambar
        ImageScrollBackground(listState)

        // Lapisan 2: Overlay Teks dan GIF (Semi Transparan)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f), // Hitam 50% transparan
                        MaterialTheme.shapes.medium
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tampilkan GIF
                GifImage(
                    gifResId = gifResId,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Tampilkan Teks
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Composable untuk menampilkan list gambar yang bisa di-scroll
@Composable
fun ImageScrollBackground(listState: LazyListState) {
    val context = LocalContext.current
    val screenHeightDp = (context.resources.displayMetrics.heightPixels / context.resources.displayMetrics.density).dp

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        // Menonaktifkan scroll pengguna
        userScrollEnabled = false
    ) {
        items(imageIds.size) { index ->
            Image(
                painter = painterResource(id = imageIds[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp) // Pastikan gambar memenuhi tinggi layar
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