package com.example.cindernela

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cindernela.ui.theme.CindernelaTheme
import com.example.cindernela.ui.theme.PastelPinkPrimary
import kotlinx.coroutines.delay
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest


class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = true // State untuk melacak status musik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupMediaPlayer()

        setContent {
            // State untuk mengelola perpindahan layar (Welcome Screen vs Birthday Screen)
            var showWelcomeScreen by remember { mutableStateOf(true) }
            // State untuk mengontrol musik dari Compose
            var uiMusicPlaying by remember { mutableStateOf(isMusicPlaying) }

            // Function untuk toggle musik
            val toggleMusic: () -> Unit = {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                    isMusicPlaying = false
                } else {
                    mediaPlayer?.start()
                    isMusicPlaying = true
                }
                uiMusicPlaying = isMusicPlaying // Update UI state
            }

            // Memastikan musik mencerminkan status awal saat Compose diluncurkan
            LaunchedEffect(uiMusicPlaying) {
                if (uiMusicPlaying && mediaPlayer?.isPlaying != true) {
                    mediaPlayer?.start()
                } else if (!uiMusicPlaying && mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            }

            CindernelaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        // Tampilkan kontrol musik hanya pada BirthdayScreen
                        if (!showWelcomeScreen) {
                            MusicControlFab(
                                isPlaying = uiMusicPlaying,
                                onClick = toggleMusic
                            )
                        }
                    }
                ) { innerPadding ->
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

    private fun setupMediaPlayer() {
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

        if (isMusicPlaying) {
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan dan bebaskan sumber daya MediaPlayer saat Activity dihancurkan
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

// =========================================================================================
// 1. COMPOSABLE BARU: Music Control Floating Action Button (FAB)
// =========================================================================================
@Composable
fun MusicControlFab(isPlaying: Boolean, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = PastelPinkPrimary,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause Music" else "Play Music"
        )
    }
}


// =========================================================================================
// 2. COMPOSABLE BARU: Special End Screen dengan Animasi (Disesuaikan)
// =========================================================================================
@Composable
fun SpecialEndScreen(modifier: Modifier = Modifier) {

    // 1. Setup untuk Falling Text
    val mainText = "Happy 21 My Lovely"
    // Pisahkan teks menjadi karakter tunggal
    val chars = remember { mainText.map { it.toString() } }

    // State untuk alpha (0f/1f) dan vertical offset (Y) untuk setiap karakter.
    // Inisialisasi: alpha 0, offset Y -50 (di atas)
    val charTargetValues = remember {
        mutableStateListOf<Pair<Float, Float>>().apply {
            repeat(chars.size) { add(0f to -50f) }
        }
    }

    // 2. Setup untuk Fade In GIF & Text
    var isFinalContentVisible by remember { mutableStateOf(false) }

    // GIFs to display on end screen
    val endScreenGifs = remember {
        listOf(R.drawable.bd5, R.drawable.bd4, R.drawable.bd1, R.drawable.bd2, R.drawable.bd3)
    }

    // Animasikan teks jatuh
    LaunchedEffect(Unit) {
        chars.forEachIndexed { index, _ ->
            // Staggered delay (100ms per karakter)
            delay(index * 100L)
            charTargetValues[index] = 1f to 0f // Target: alpha 1, offset Y 0 (posisi akhir)
        }

        // Atur delay untuk konten final (setelah animasi utama selesai)
        val totalDelayMs = chars.size * 100L + 500
        delay(totalDelayMs)
        isFinalContentVisible = true // Triggers the final content fade in
    }

    // Animate alpha untuk efek fade-in pada konten final (GIFs dan Teks)
    val finalAlpha by animateFloatAsState(
        targetValue = if (isFinalContentVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), // 1 detik fade in
        label = "finalContentFade"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            // Overlay hitam yang tebal untuk fokus ke pesan
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(24.dp), // Padding dikurangi sedikit
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // FALLING TEXT Container (Happy 21 My Lovely)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(12.dp) // Padding dikurangi
                    .wrapContentWidth()
            ) {
                chars.forEachIndexed { index, char ->
                    // Animasi properti dari state
                    val alphaState by animateFloatAsState(
                        targetValue = charTargetValues[index].first,
                        animationSpec = tween(durationMillis = 500, delayMillis = 0),
                        label = "charAlpha${index}"
                    )

                    val offsetY by animateFloatAsState(
                        targetValue = charTargetValues[index].second,
                        animationSpec = tween(durationMillis = 500, delayMillis = 0),
                        label = "charOffsetY${index}"
                    )

                    Text(
                        text = char,
                        color = Color.White,
                        fontSize = 28.sp, // Ukuran diperkecil (dari 32.sp)
                        fontFamily = FontFamily.Default, // Ensure a default font is used
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        // Gunakan graphicsLayer untuk animasi performa tinggi
                        modifier = Modifier.graphicsLayer(
                            alpha = alphaState,
                            // Angka 5 adalah multiplier untuk membuat gerakan jatuh lebih terlihat
                            translationY = offsetY * 5
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp)) // Jarak dikurangi

            // FADE IN GIFS & TEXT (Appears after falling text is done)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                // Apply Fade In to the whole final block
                modifier = Modifier.graphicsLayer(alpha = finalAlpha)
            ) {
                // GIF Row
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp) // Padding horizontal dikurangi
                ) {
                    endScreenGifs.forEach { gifId ->
                        GifImage(gifResId = gifId, modifier = Modifier.size(50.dp)) // Ukuran GIF diperkecil (dari 60.dp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp)) // Jarak dikurangi

                // Final Text
                Text(
                    text = "Continued in 12.02",
                    color = PastelPinkPrimary,
                    fontSize = 24.sp, // Ukuran diperkecil (dari 28.sp)
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        }
    }
}


// =========================================================================================
// 3. COMPOSABLE BARU: Welcome Screen
// =========================================================================================
@Composable
fun WelcomeScreen(onTapToContinue: () -> Unit, modifier: Modifier = Modifier) {
    // Ambil semua GIF IDs dari birthdayItems
    val allGifIds = remember { birthdayItems.map { it.second } }
    val topGifs = allGifIds.take(3)
    val bottomGifs = allGifIds.takeLast(2)

    Box(
        modifier = modifier
            .fillMaxSize()
            // Gunakan warna Pink Pastel sebagai latar belakang
            .background(color = PastelPinkPrimary)
            .clickable(onClick = onTapToContinue), // Tombol 'tap' untuk melanjutkan
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // 3 GIF di atas tulisan "Aloo, selamat datang yay"
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                topGifs.forEach { gifId ->
                    GifImage(gifResId = gifId, modifier = Modifier.size(80.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Aloo, selamat datang yay",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "tekan kalau kepo",
                fontSize = 20.sp,
                // Beri sedikit transparansi agar terlihat elegan
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2 GIF di bawah tulisan "tekan kalau kepo"
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp)
            ) {
                bottomGifs.forEach { gifId ->
                    GifImage(gifResId = gifId, modifier = Modifier.size(80.dp))
                }
            }
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
    R.drawable.yay16, R.drawable.yay17,
    // Baris ini sengaja dihilangkan untuk menghemat ruang dan fokus pada kode yang diperbaiki
    R.drawable.yay18, R.drawable.yay19, R.drawable.yay20, R.drawable.yay21, R.drawable.yay22,
    R.drawable.yay23, R.drawable.yay24, R.drawable.yay25, R.drawable.yay26, R.drawable.yay27,
    R.drawable.yay28, R.drawable.yay29, R.drawable.yay30, R.drawable.yay31, R.drawable.yay32,
    R.drawable.yay33, R.drawable.yay34, R.drawable.yay35, R.drawable.yay36, R.drawable.yay37,
    R.drawable.yay38, R.drawable.yay39, R.drawable.yay40, R.drawable.yay41, R.drawable.yay42,
    R.drawable.yay43, R.drawable.yay44, R.drawable.yay45, R.drawable.yay46, R.drawable.yay47,
    R.drawable.yay48, R.drawable.yay49, R.drawable.yay50,
    R.drawable.yay51, R.drawable.yay52, R.drawable.yay53, R.drawable.yay54, R.drawable.yay55,
    R.drawable.yay56, R.drawable.yay57, R.drawable.yay58, R.drawable.yay59, R.drawable.yay60,
    R.drawable.yay61, R.drawable.yay62, R.drawable.yay63, R.drawable.yay64, R.drawable.yay65,
    R.drawable.yay66, R.drawable.yay67, R.drawable.yay68, R.drawable.yay69, R.drawable.yay70,
)

// Data GIF yang sudah ada
// R.drawable.bd5: Bear dengan mawar & mahkota (💖)
// R.drawable.bd4: Bear berpelukan (✨)
// R.drawable.bd1: Bear dengan kue ultah (👑)
// R.drawable.bd2: Bear berpegangan tangan (😊)
// R.drawable.bd3: Bear mencium (❤️)

val birthdayItems = listOf(
    Pair("Selamat 21 Happy tahun birthday yang ke 21💖", R.drawable.bd5),
    Pair("itulah pokoknnya yawww ✨", R.drawable.bd4),
    Pair("Semoga sehat selalu, Aaaamiiinn 👑", R.drawable.bd1),
    Pair("Semoga rezekinya lancar selalu, Aaaamiiinn😊", R.drawable.bd2),
    Pair("Semoga segala urusannya dimudahkan selalu, Aaaamiiinn ❤️", R.drawable.bd3),

    Pair("Ini udah kepo aja apa udah kepo banget?💖", R.drawable.bd5),
    Pair("Mencetnya pelan-pelan ya, jangan cepet-cepet, nanti ke skip ✨", R.drawable.bd4),
    Pair("...", R.drawable.bd1),
    Pair("Pasti kepo banget yawww 😊", R.drawable.bd2),
    Pair("Gaada apa-apa si, cuma mau bilang... ❤️", R.drawable.bd3),

    Pair("Selamat ulang tahun, Sayang atu yang paling atu sayangi 💖", R.drawable.bd5),
    Pair("Semoga di umur yang sekarang bisa jauh lebih baik agi yawww ✨", R.drawable.bd4),
    Pair("Semoga semua keinginan tamu dan kita tercapai yaw. Aaaamiiinn 👑", R.drawable.bd1),
    Pair("Setiap bersama tamu ada banyak cerita yang gamungkin bisa dilupain. Happy Birthday My Love! 😊", R.drawable.bd2),
    Pair("Maaciw udh selalu jadi yang terbaik yaaww. Selamat ulang tahun, cintaku. ❤️", R.drawable.bd3),
    // Tambahan 15 Ucapan baru untuk mencapai 20 total
    Pair("Maaciw udah jadi sosok bocil yang selalu bikin rame hidup atu 💖", R.drawable.bd5),
    Pair("Maaciw udah jadi sosok wanita yang dewasa(walau kadang-kadang aja cuma) yang supportif ✨", R.drawable.bd4),
    Pair("Maaciw udah jadi yang selalu ada buat dengerin sisi lainnya atu 👑", R.drawable.bd1),
    Pair("Maaciw udah jadi alasan atu untuk tidak boleh pasrah dan menyerah 😊", R.drawable.bd2),
    Pair("Maaciw udah jadi sandaran saat atu udah gakuat berdiri ❤️", R.drawable.bd3),

    Pair("Maaciw udah jadi orang yang sabar dengerin hal-hal aneh dari mulut sampah ini 💖", R.drawable.bd5),
    Pair("Maaciw udah jadi orang yang mau ikutan berkembang sama atu yang belum bisa ngapa-ngapain ✨", R.drawable.bd4),
    Pair("Maaciw udah mau jalan beriringan dengan pria biasa ini👑", R.drawable.bd1),
    Pair("Maaciw udah lahir dan memilih atu untuk bisa sama tamu 😊", R.drawable.bd2),
    Pair("Cinta terbaikku lahir ternyata pas shopee gini. Selamat ulang tahun sayang! ❤️", R.drawable.bd3),

    Pair("Kalau bukan sama tamu, wadooo udah gatu lagi deh 💖", R.drawable.bd5),
    Pair("Ya cuma ini dan itu(benda pink) aja yang bisa atu kasih untuk sekarang. Sorry and Love you! ✨", R.drawable.bd4),
    Pair("Hadiahnya udah dibuka belom? harusnya kan baca ini dulu ya baru boleh buka 👑", R.drawable.bd1),
    Pair("Gaboleh curang loh ya, tamu biasanya curangnyoooii. Dengerin lagunya sampe habis yaww 😊", R.drawable.bd2),
    Pair("Pokoknya atu cuma mau minta maaf karena hadiahnya ga seberapa ❤️", R.drawable.bd3),

    Pair("Minta maaf juga karena gabisa jadi romantis kayak orang-orang, tapi atu usahain kok 💖", R.drawable.bd5),
    Pair("Minta maaf juga belom ngasih apa-apa yang berarti kali ✨", R.drawable.bd4),
    Pair("Tapi atu usahain kedepannya yaa sayang yaa 👑", R.drawable.bd1),
    Pair("Tapi tamu temenin ya, masa tega ninggalin atu sendirian, ga kan? 😊", R.drawable.bd2),
    Pair("Masih banyak impian tamu, atu, dan kita yang belum tercapai. ❤️", R.drawable.bd3),

    // 5 Item tambahan untuk total 20
    Pair("Jalan kedepannya masih panjaaannggggggggggg kaliiiiii💖", R.drawable.bd5),
    Pair("Kita usahain semuanya bareng ya, cuma atu, tamu, dan kita✨", R.drawable.bd4),
    Pair("Karena ada seseorang yang nyadarin atu, impian tu gaada yang rendah atau tinggi 👑", R.drawable.bd1),
    Pair("Semua mimpi punya arti dan punya jalannya masing-masing 😊", R.drawable.bd2),
    Pair("Cintaku, selamat ulang tahun yang ke-100 eh ke-21! ❤️", R.drawable.bd3)
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
    val totalMessages = birthdayItems.size

    // State baru untuk mengelola tampilan layar penutup
    var showEndScreen by remember { mutableStateOf(false) }

    val currentItem = birthdayItems[currentMessageIndex]

    // Logika untuk navigasi pesan
    val onNextMessage = {
        if (currentMessageIndex == totalMessages - 1) {
            // Jika pesan terakhir, pindah ke layar penutup
            showEndScreen = true
        } else {
            // Jika belum pesan terakhir, lanjut ke pesan berikutnya
            currentMessageIndex += 1
        }
    }
    val onPreviousMessage = {
        currentMessageIndex = if (currentMessageIndex > 0) currentMessageIndex - 1 else 0
    }

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
        modifier = modifier.fillMaxSize()
    ) {
        // Lapisan 1: Scrolling Gambar (tetap berjalan di latar belakang)
        ImageScrollBackground(listState)

        // Lapisan 2: Overlay Konten (Pesan atau Penutup Khusus)
        if (showEndScreen) {
            SpecialEndScreen(modifier = Modifier.fillMaxSize())
        } else {
            // Content for standard message view
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
                        .padding(20.dp), // Padding dikurangi dari 24.dp
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Indikator Pesan
                    Text(
                        text = "Pesan ${currentMessageIndex + 1} dari $totalMessages",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Tampilkan GIF dan Teks dengan Crossfade (Pergantian Transisi Halus)
                    Crossfade(targetState = currentItem, label = "message_crossfade") { item ->
                        val fullMessage = item.first
                        // State untuk menahan teks yang sedang di-typing
                        var displayedText by remember { mutableStateOf("") }

                        // Efek Mengetik: Dijalankan ulang setiap kali item (pesan/GIF) berubah
                        LaunchedEffect(fullMessage) {
                            displayedText = "" // Reset teks
                            for (i in fullMessage.indices) {
                                displayedText += fullMessage[i]
                                delay(40) // Kecepatan mengetik: 40ms per karakter
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Tampilkan GIF
                            GifImage(
                                gifResId = item.second,
                                modifier = Modifier.size(80.dp) // Ukuran diperkecil (dari 100.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp)) // Jarak diperkecil
                            // Tampilkan Teks yang sedang dianimasikan
                            Text(
                                text = displayedText,
                                color = Color.White,
                                fontSize = 20.sp, // Ukuran diperkecil (dari 24.sp)
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp)) // Jarak diperkecil

                    // Kontrol Navigasi (Previous dan Next/Finish)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tombol 'Previous' hanya muncul jika bukan pesan pertama
                        if (currentMessageIndex > 0) {
                            Button(
                                onClick = onPreviousMessage,
                                // Content padding is implicitly handled by Button size, focusing on text/icon size for smaller buttons
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = PastelPinkPrimary.copy(alpha = 0.8f))
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Message", modifier = Modifier.size(20.dp)) // Ikon diperkecil
                                Spacer(Modifier.width(4.dp)) // Jarak diperkecil
                                Text("Sebelumnya", fontSize = 14.sp) // Teks diperkecil
                            }
                        } else {
                            // Placeholder (Spacer kecil) agar tombol 'Selanjutnya' tetap di kanan
                            Spacer(modifier = Modifier.size(1.dp))
                        }

                        // Teks tombol diubah berdasarkan index
                        val isLastMessage = currentMessageIndex == totalMessages - 1
                        val buttonText = if (isLastMessage) "Selesaikan" else "Selanjutnya"

                        Button(
                            onClick = onNextMessage, // Menggunakan logika onNextMessage yang sudah diperbarui
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = PastelPinkPrimary)
                        ) {
                            Text(buttonText, fontSize = 14.sp) // Teks diperkecil
                            Spacer(Modifier.width(4.dp)) // Jarak diperkecil
                            // Hanya tampilkan ikon panah jika BUKAN tombol Selesaikan
                            if (!isLastMessage) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Message", modifier = Modifier.size(20.dp)) // Ikon diperkecil
                            }
                        }
                    }
                }
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
                // Beri sedikit opacity agar overlay teks lebih dominan
                alpha = 0.8f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeightDp)
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