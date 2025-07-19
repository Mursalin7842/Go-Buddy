package mursalin.companion.gobuddy.presentation.screens.`01_splash`

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import mursalin.companion.gobuddy.R
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme
import kotlin.random.Random

// Enum to manage the different animation states
private enum class SplashAnimState {
    JUMPING, CENTERING, ZOOMING
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    var animState by remember { mutableStateOf(SplashAnimState.JUMPING) }

    // --- Animation States ---
    var targetOffsetX by remember { mutableStateOf(0.dp) }
    var targetOffsetY by remember { mutableStateOf(0.dp) }

    // Animate the X and Y offsets for the jumping and centering effect
    val animatedOffsetX by animateDpAsState(
        targetValue = targetOffsetX,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 200f),
        label = "SplashOffsetX"
    )
    val animatedOffsetY by animateDpAsState(
        targetValue = targetOffsetY,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 200f),
        label = "SplashOffsetY"
    )

    // Animate the scale for the final zoom-in effect
    val animatedScale by animateFloatAsState(
        targetValue = if (animState == SplashAnimState.ZOOMING) 8f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "SplashScale"
    )

    // Animate alpha to fade out during the zoom
    val animatedAlpha by animateFloatAsState(
        targetValue = if (animState == SplashAnimState.ZOOMING) 0f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "SplashAlpha"
    )

    // --- ExoPlayer Setup ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.splash_video}")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    // --- Animation Choreography ---
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        // Phase 1: Jump around randomly
        repeat(5) { // Perform 5 jumps
            targetOffsetX = Random.nextInt(-80, 80).dp
            targetOffsetY = Random.nextInt(-150, 150).dp
            delay(300)
        }

        // Phase 2: Come to the center
        animState = SplashAnimState.CENTERING
        targetOffsetX = 0.dp
        targetOffsetY = 0.dp
        delay(1000) // Wait for it to settle in the center

        // Phase 3: Zoom in to finish
        animState = SplashAnimState.ZOOMING
        delay(800) // Wait for zoom animation to complete

        // Finish the splash
        onSplashFinished()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // --- UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5c10ff)), // Custom background color
        contentAlignment = Alignment.Center
    ) {
        // The AndroidView hosts the video player. We animate its offset and scale.
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false // Hide video controls
                    layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .size(width = screenWidthDp * 0.7f, height = screenWidthDp * 0.7f)
                .offset(x = animatedOffsetX, y = animatedOffsetY)
                .scale(animatedScale)
                .alpha(animatedAlpha)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GoBuddyTheme {
        // Note: The video player and animations will not work correctly in this preview.
        // It will likely appear as a blank screen, which is expected.
        SplashScreen(onSplashFinished = {})
    }
}