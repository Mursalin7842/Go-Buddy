package mursalin.companion.gobuddy.presentation.screens.s10settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import mursalin.companion.gobuddy.domain.model.Theme
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme
import mursalin.companion.gobuddy.presentation.viewmodel.AuthViewModel
import mursalin.companion.gobuddy.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentTheme by settingsViewModel.theme.collectAsState()
    val authState by authViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Account",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card {
                    Column {
                        ProfileInfo(label = "Name", value = authState.user?.name ?: "Loading...")
                        Divider()
                        ProfileInfo(label = "Email", value = authState.user?.email ?: "Loading...")
                    }
                }
            }

            item {
                Text(
                    "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ThemeSelector(
                    currentTheme = currentTheme,
                    onThemeSelected = { settingsViewModel.onThemeChange(it) }
                )
            }

            item {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout Icon", modifier = Modifier.padding(end = 8.dp))
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun ProfileInfo(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Person, contentDescription = "$label Icon", modifier = Modifier.padding(end = 16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelector(
    currentTheme: Theme,
    onThemeSelected: (Theme) -> Unit
) {
    Card {
        Column {
            ThemeOption(
                text = "Light",
                isSelected = currentTheme == Theme.LIGHT,
                onClick = { onThemeSelected(Theme.LIGHT) }
            )
            Divider()
            ThemeOption(
                text = "Dark",
                isSelected = currentTheme == Theme.DARK,
                onClick = { onThemeSelected(Theme.DARK) }
            )
            Divider()
            ThemeOption(
                text = "System Default",
                isSelected = currentTheme == Theme.SYSTEM,
                onClick = { onThemeSelected(Theme.SYSTEM) }
            )
        }
    }
}

@Composable
fun ThemeOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, modifier = Modifier.weight(1f))
        if (isSelected) {
            Icon(
                imageVector = when (text) {
                    "Light" -> Icons.Default.LightMode
                    "Dark" -> Icons.Default.DarkMode
                    else -> Icons.Default.Settings
                },
                contentDescription = "$text theme selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    GoBuddyTheme {
        SettingsScreen(
            navController = NavController(LocalContext.current),
            onLogout = {}
        )
    }
}