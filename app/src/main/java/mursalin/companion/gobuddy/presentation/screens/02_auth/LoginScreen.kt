package mursalin.companion.gobuddy.presentation.screens.`02_auth`

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.RiveFileController
import app.rive.runtime.kotlin.core.Rive
import mursalin.companion.gobuddy.R
import mursalin.companion.gobuddy.presentation.theme.GoBuddyTheme

// Added missing imports for property delegates
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State holders for Rive animation inputs
    var isChecking by remember { mutableStateOf(false) }
    var isHandsUp by remember { mutableStateOf(false) }

    // State holder for the Rive animation controller, using the correct RiveFileController type
    var riveController by remember { mutableStateOf<RiveFileController?>(null) }
    val stateMachineName = "State Machine 1"

    // Initialize Rive
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Rive.init(context)
    }

    GoBuddyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Rive Animation View
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    AndroidView(
                        factory = { ctx ->
                            RiveAnimationView(ctx).apply {
                                // Set the Rive resource and specify the state machine name
                                setRiveResource(
                                    R.raw.login_animation,
                                    stateMachineName = stateMachineName,
                                    autoplay = true
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { view ->
                            // Store the controller for use outside the update block (e.g., in onClick)
                            if (riveController == null) {
                                riveController = view.controller
                            }

                            // Update the state machine's inputs using the public controller methods
                            view.controller.setBooleanState(stateMachineName, "isChecking", isChecking)
                            view.controller.setBooleanState(stateMachineName, "isHandsUp", isHandsUp)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Log in to continue your journey.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isChecking = it.isNotEmpty()
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            // When focus changes on the email field, the hands should not be up
                            isHandsUp = false
                        },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            // Hands go up only when the password field is focused
                            isHandsUp = focusState.isFocused
                        },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot Password
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle forgot password */ },
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        isHandsUp = false
                        isChecking = false

                        // Fire triggers using the public controller method
                        if (email == "admin" && password == "admin") {
                            riveController?.fireTrigger(stateMachineName, "success")
                        } else {
                            riveController?.fireTrigger(stateMachineName, "fail")
                        }

                        onLoginClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Login", fontSize = 16.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign Up Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign Up",
                        modifier = Modifier.clickable(onClick = onSignUpClick),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(
        onLoginClick = {},
        onSignUpClick = {}
    )
}
