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
import androidx.compose.ui.draw.alpha
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
import kotlin.math.cos
import kotlin.math.sin

// Enum to manage the different animation states
private enum class SplashAnimState {
    ENTERING, // Dropping in from top, upside down
    CIRCLING, // Moving in a circular path and rotating to be upright
    ZOOMING   // Final zoom and fade
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    var animState by remember { mutableStateOf(SplashAnimState.ENTERING) }

    // --- Animation States ---
    var angle by remember { mutableStateOf(0f) }
    var targetRotation by remember { mutableStateOf(180f) }

    // Animate the angle for the circular motion
    val animatedAngle by animateFloatAsState(
        targetValue = angle,
        animationSpec = tween(durationMillis = 2000), // 2 seconds for circular motion
        label = "SplashAngle"
    )

    // Animate the rotation
    val animatedRotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(durationMillis = 2000), // Match circular motion duration
        label = "SplashRotation"
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

    // Calculate circular path offsets based on the animated angle
    val radius = 120.dp
    val offsetX = (radius.value * cos(Math.toRadians(animatedAngle.toDouble()))).dp
    val offsetY = (radius.value * sin(Math.toRadians(animatedAngle.toDouble()))).dp

    LaunchedEffect(Unit) {
        // Phase 1: Drop in from top (handled by initial state)
        delay(100) // Small delay to start animations

        // Phase 2: Start circular motion and rotation to upright
        animState = SplashAnimState.CIRCLING
        angle = 720f      // Two full circles
        targetRotation = 0f // Rotate to be upright
        delay(2200) // Wait for circling to finish

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
        // The AndroidView hosts the video player. We use graphicsLayer for efficient animation.
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
                .graphicsLayer {
                    // Apply all transformations here
                    translationX = if (animState == SplashAnimState.CIRCLING) offsetX.toPx() else 0f
                    translationY = if (animState == SplashAnimState.CIRCLING) offsetY.toPx() else 0f
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
        // Note: The complex animations and video player will not work correctly in this preview.
        // It will likely appear as a blank screen, which is expected.
        SplashScreen(onSplashFinished = {})
    }
}