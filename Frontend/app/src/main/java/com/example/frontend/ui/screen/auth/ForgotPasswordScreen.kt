package com.example.frontend.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    // go with true when connect with real api
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (!submitted) {

                // Email icon circle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(AvatarCircleColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        modifier = Modifier.size(52.dp),
                        tint = Color(0xFF5A6A8A)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Forgot Password?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your email and we'll send you\na link to reset your password.",
                    fontSize = 14.sp,
                    color = HintTextColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email field
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

                Spacer(modifier = Modifier.height(24.dp))

                // Submit gradient button
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
                    Button(
                        onClick = { if (email.isNotEmpty()) submitted = true },
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                    ) {
                        Text(
                            text = "Send Reset Link",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            } else {

                // Success state
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓", fontSize = 48.sp, color = Color(0xFF4CAF50))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Check your email!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We sent a password reset link to\n$email",
                    fontSize = 14.sp,
                    color = HintTextColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Back to login button
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
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(50),
                        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                    ) {
                        Text(
                            text = "Back to Login",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ForgotPasswordPreview() {
    ForgotPasswordScreen()
}