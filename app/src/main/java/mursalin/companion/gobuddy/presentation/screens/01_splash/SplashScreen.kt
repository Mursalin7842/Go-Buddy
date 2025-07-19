package mursalin.companion.gobuddy.presentation.screens.`01_splash`

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    IDLE,      // Off-screen
    FALLING,   // Falling from the sky, upside down
    ROAMING,   // Moving randomly while rotating
    CENTERING, // Moving back to the center
    VISIBLE,   // Centered and upright
    ZOOMING    // Final zoom and fade
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    var animState by remember { mutableStateOf(SplashAnimState.IDLE) }

    // --- Animation States ---
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val screenWidthDp = configuration.screenWidthDp.dp

    var targetOffsetX by remember { mutableStateOf(0.dp) }
    var targetOffsetY by remember { mutableStateOf(-screenHeightDp) } // Start off-screen top
    var targetRotation by remember { mutableStateOf(180f) } // Start upside down

    // Animate position and rotation
    val animatedOffsetX by animateDpAsState(targetValue = targetOffsetX, animationSpec = spring(dampingRatio = 0.6f, stiffness = 150f), label = "SplashOffsetX")
    val animatedOffsetY by animateDpAsState(targetValue = targetOffsetY, animationSpec = spring(dampingRatio = 0.6f, stiffness = 150f), label = "SplashOffsetY")
    val animatedRotation by animateFloatAsState(targetValue = targetRotation, animationSpec = tween(durationMillis = 1500), label = "SplashRotation")

    // Animate the final zoom and fade
    val animatedScale by animateFloatAsState(targetValue = if (animState == SplashAnimState.ZOOMING) 8f else 1f, animationSpec = tween(800), label = "SplashScale")
    val animatedAlpha by animateFloatAsState(targetValue = if (animState == SplashAnimState.ZOOMING) 0f else 1f, animationSpec = tween(800), label = "SplashAlpha")

    // --- ExoPlayer Setup ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.splash_video}")
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    // --- Animation Choreography ---
    LaunchedEffect(Unit) {
        // Phase 1: Fall from the sky to the center
        animState = SplashAnimState.FALLING
        targetOffsetY = 0.dp
        delay(1000)

        // Phase 2: Roam randomly while rotating to be upright
        animState = SplashAnimState.ROAMING
        targetRotation = 0f // Start rotating to upright position
        repeat(4) { // Perform 4 random movements
            targetOffsetX = Random.nextInt(-80, 80).dp
            targetOffsetY = Random.nextInt(-120, 120).dp
            delay(400)
        }

        // Phase 3: Return to center
        animState = SplashAnimState.CENTERING
        targetOffsetX = 0.dp
        targetOffsetY = 0.dp
        delay(1000) // Wait to settle

        // Phase 4: Hold in the center for 2 seconds
        animState = SplashAnimState.VISIBLE
        delay(2000)

        // Phase 5: Zoom in to finish
        animState = SplashAnimState.ZOOMING
        delay(800)

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
            .background(Color(0xFF5c10ff)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .size(width = screenWidthDp * 0.7f, height = screenWidthDp * 0.7f)
                .graphicsLayer {
                    translationX = animatedOffsetX.toPx()
                    translationY = animatedOffsetY.toPx()
                    rotationZ = animatedRotation
                    scaleX = animatedScale
                    scaleY = animatedScale
                    alpha = animatedAlpha
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GoBuddyTheme {
        SplashScreen(onSplashFinished = {})
    }
}