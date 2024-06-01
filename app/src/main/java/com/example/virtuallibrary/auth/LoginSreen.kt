package com.example.virtuallibrary.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.virtuallibrary.data.AuthRepository
import com.example.virtuallibrary.ui.theme.DirtyWhite
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.ui.theme.comfortaaLight
import com.example.virtuallibrary.ui.theme.robotoBold
import java.security.AccessControlException

@Composable
fun LoginScreen(
    navController: NavController,
    authRepository: AuthRepository,
    onLoginSuccess: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Log in",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)

            )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DirtyWhite,
                unfocusedContainerColor = DirtyWhite,
                focusedTextColor = GreenColor,
                unfocusedTextColor = GreenColor,
                focusedIndicatorColor = GreenColor,
                unfocusedIndicatorColor = GreenColor,
                cursorColor = GreenColor,
                unfocusedLabelColor = GreenColor,
                focusedLabelColor = GreenColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DirtyWhite,
                unfocusedContainerColor = DirtyWhite,
                focusedTextColor = GreenColor,
                unfocusedTextColor = GreenColor,
                focusedIndicatorColor = GreenColor,
                unfocusedIndicatorColor = GreenColor,
                cursorColor = GreenColor,
                unfocusedLabelColor = GreenColor,
                focusedLabelColor = GreenColor
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                authRepository.loginUser(email, password) { success, msg ->
                    if (success) {
                        onLoginSuccess(email, password)
                    } else {
                        message = msg ?: "Incorrect password or email."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenColor,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(6.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                "LOG IN",
                style = TextStyle(
                    fontFamily = robotoBold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}




