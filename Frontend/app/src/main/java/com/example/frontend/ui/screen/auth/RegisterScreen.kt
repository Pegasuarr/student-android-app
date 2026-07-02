package com.example.frontend.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundColor      = Color(0xFFF0F4FF)
private val AvatarCircleColor    = Color(0xFFDDE6F5)
private val PrimaryGradientStart = Color(0xFF6C63FF)
private val PrimaryGradientEnd   = Color(0xFF48B4FF)
private val TextFieldBorderColor = Color(0xFFCCCCCC)
private val HintTextColor        = Color(0xFFAAAAAA)
private val TextPrimaryColor     = Color(0xFF1A1A2E)
private val LinkColor            = Color(0xFF6C63FF)
private val ErrorColor           = Color(0xFFE53935)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRegisterClick: (String, String, String) -> Unit = { _, _, _ -> },
    onLoginClick: () -> Unit = {}
) {
    var fullName    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var pw          by remember { mutableStateOf("") }
    var cfPw        by remember { mutableStateOf("") }
    var pwVisible   by remember { mutableStateOf(false) }
    var cfPwVisible by remember { mutableStateOf(false) }

    val passwordMismatch = cfPw.isNotEmpty() && pw != cfPw

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onLoginClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(AvatarCircleColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF5A6A8A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Register Account",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryColor
            )

            Text(
                text = "Fill in the details below to register",
                fontSize = 13.sp,
                color = HintTextColor
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text(text = "Full Name", color = HintTextColor) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = TextFieldBorderColor,
                    focusedBorderColor = PrimaryGradientStart,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email", color = HintTextColor) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = TextFieldBorderColor,
                    focusedBorderColor = PrimaryGradientStart,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = pw,
                onValueChange = { pw = it },
                placeholder = { Text(text = "Password", color = HintTextColor) },
                singleLine = true,
                visualTransformation = if (pwVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { pwVisible = !pwVisible }) {
                        Icon(
                            imageVector = if (pwVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (pwVisible) "Hide password" else "Show password",
                            tint = HintTextColor
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = TextFieldBorderColor,
                    focusedBorderColor = PrimaryGradientStart,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password
            OutlinedTextField(
                value = cfPw,
                onValueChange = { cfPw = it },
                placeholder = { Text(text = "Confirm Password", color = HintTextColor) },
                singleLine = true,
                isError = passwordMismatch,
                visualTransformation = if (cfPwVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { cfPwVisible = !cfPwVisible }) {
                        Icon(
                            imageVector = if (cfPwVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (cfPwVisible) "Hide password" else "Show password",
                            tint = HintTextColor
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (passwordMismatch) ErrorColor else TextFieldBorderColor,
                    focusedBorderColor = if (passwordMismatch) ErrorColor else PrimaryGradientStart,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (passwordMismatch) {
                Text(
                    text = "Passwords do not match",
                    color = ErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = ErrorColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Register gradient button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PrimaryGradientStart, PrimaryGradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Button(
                        onClick = {
                            if (pw == cfPw && fullName.isNotEmpty() && email.isNotEmpty() && pw.isNotEmpty()) {
                                onRegisterClick(fullName, email, pw)
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                    ) {
                        Text(
                            text = "Register",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Already have an account? Log In
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onLoginClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = " Log In",
                        color = LinkColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterPreview() {
    RegisterScreen()
}