package com.example.frontend.ui.screen.auth

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

// Colors
private val BackgroundColor        = Color(0xFFF0F4FF)
private val AvatarCircleColor      = Color(0xFFDDE6F5)
private val PrimaryGradientStart   = Color(0xFF6C63FF)
private val PrimaryGradientEnd     = Color(0xFF48B4FF)
private val GoogleButtonBorder     = Color(0xFFDDDDDD)
private val TextFieldBorderColor   = Color(0xFFCCCCCC)
private val HintTextColor          = Color(0xFFAAAAAA)
private val TextPrimaryColor       = Color(0xFF1A1A2E)
private val LinkColor              = Color(0xFF6C63FF)
private val ErrorColor             = Color(0xFFE53935)

@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onGoogleSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AvatarCircleColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Profile Icon",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF5A6A8A)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // title
        Text(
            text = "Login Screen",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryColor
        )

        Spacer(modifier = Modifier.height(28.dp))

        // email
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

        // pw
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Password", color = HintTextColor) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
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

        // Forgot pw
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = "Forgot Password?",
                    color = LinkColor,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = ErrorColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Log In gradient button
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
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                ) {
                    Text(
                        text = "Log In",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Google Sign In
        OutlinedButton(
            onClick = onGoogleSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, GoogleButtonBorder),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(
                text = "G",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4285F4)
            )
            Text(
                text = "  Google Sign In",
                fontSize = 15.sp,
                color = TextPrimaryColor,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // dont have an account? Register
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account?",
                color = Color(0xFF666666),
                fontSize = 14.sp
            )
            TextButton(
                onClick = onRegisterClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = " Register",
                    color = LinkColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen()
}