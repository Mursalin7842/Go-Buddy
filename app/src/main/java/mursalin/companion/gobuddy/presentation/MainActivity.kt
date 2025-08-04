package mursalin.companion.gobuddy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mursalin.companion.gobuddy.presentation.navigation.AppNavigation
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme

// PRODUCTION: Annotate the main activity to enable field injection.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoBuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AppNavigation composable is the entry point for the UI
                    AppNavigation()
                }
            }
        }
    }
}