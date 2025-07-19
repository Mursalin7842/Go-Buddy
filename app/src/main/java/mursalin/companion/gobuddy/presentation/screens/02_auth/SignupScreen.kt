package mursalin.companion.gobuddy.presentation.screens.`02_auth`

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mursalin.companion.gobuddy.presentation.viewmodel.AuthEvent
import mursalin.companion.gobuddy.presentation.viewmodel.AuthState

@Composable
fun SignUpScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Join us to get started", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(value = state.fullName, onValueChange = { onEvent(AuthEvent.FullNameChanged(it)) }, label = { Text("Full Name") }, leadingIcon = { Icon(Icons.Default.Person, "Full Name") }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = state.email, onValueChange = { onEvent(AuthEvent.EmailChanged(it)) }, label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, "Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = state.password, onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) }, label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, "Password") }, visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = { IconButton(onClick = { onEvent(AuthEvent.TogglePasswordVisibility) }) { Icon(if (state.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Toggle password") } }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = state.confirmPassword, onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) }, label = { Text("Confirm Password") }, leadingIcon = { Icon(Icons.Default.Lock, "Confirm Password") }, visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = { IconButton(onClick = { onEvent(AuthEvent.ToggleConfirmPasswordVisibility) }) { Icon(if (state.isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Toggle confirm password") } }, singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onEvent(AuthEvent.SignUp) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Sign Up", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        ClickableText(
            text = AnnotatedString("Already have an account? Login"),
            onClick = { onNavigateToLogin() },
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        state = AuthState(),
        onEvent = {},
        onNavigateToLogin = {}
    )
}