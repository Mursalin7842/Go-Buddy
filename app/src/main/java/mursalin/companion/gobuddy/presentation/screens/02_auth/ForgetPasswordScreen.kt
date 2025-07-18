package mursalin.companion.gobuddy.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mursalin.companion.gobuddy.presentation.theme.GOBuddyTheme
import mursalin.companion.gobuddy.presentation.viewmodel.AuthEvent
import mursalin.companion.gobuddy.presentation.viewmodel.AuthState

@Composable
fun ForgotPasswordScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your email to receive instructions",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        // --- Email Input ---
        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // --- Send Instructions Button ---
        Button(
            onClick = { /* TODO: Add event for password reset */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(text = "Send Instructions", fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // --- Back to Login Navigation ---
        ClickableText(
            text = AnnotatedString("Remember your password? Login"),
            onClick = { onNavigateBack() },
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    GOBuddyTheme {
        Surface {
            ForgotPasswordScreen(state = AuthState(), onEvent = {}, onNavigateBack = {})
        }
    }
}
