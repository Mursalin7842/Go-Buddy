package mursalin.companion.gobuddy.presentation.screens.`01_splash`

import android.net.Uri
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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

// Enum to manage the different animation states of the splash screen
private enum class SplashState {
    IDLE, POP_IN, VISIBLE, FADE_OUT
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    var splashState by remember { mutableStateOf(SplashState.IDLE) }

    // --- Animation States ---
    val screenHeight = with(LocalDensity.current) {
        LocalContext.current.resources.displayMetrics.heightPixels.toDp()
    }

    // Animate the Y offset for the pop-in effect
    val animatedOffsetY by animateDpAsState(
        targetValue = when (splashState) {
            SplashState.IDLE -> screenHeight // Start below the screen
            else -> 0.dp // Move to the center
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SplashOffsetY"
    )

    // Animate the alpha for the fade-out effect
    val animatedAlpha by animateFloatAsState(
        targetValue = if (splashState == SplashState.FADE_OUT) 0f else 1f,
        animationSpec = tween(durationMillis = 1000),
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

    // --- Lifecycle & Animation Choreography ---
    LaunchedEffect(Unit) {
        splashState = SplashState.POP_IN // Start the pop-in animation
        delay(1000) // Wait for 1 second while video is visible
        splashState = SplashState.FADE_OUT // Start the fade-out animation
        delay(1000) // Wait for fade-out to complete
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
            .background(Color(0xFF5c10ff)) // Set the custom background color
            .alpha(animatedAlpha), // Apply fade-out to the whole screen
        contentAlignment = Alignment.Center
    ) {
        // The AndroidView hosts the video player. We animate its offset.
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
                .fillMaxWidth(0.8f) // Make video take 80% of screen width
                .offset(y = animatedOffsetY)
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