package com.example.virtuallibrary.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.virtuallibrary.ui.theme.DirtyWhite
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.ui.theme.robotoBold

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by authViewModel.authState.observeAsState()

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
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.signIn(email, password)
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT)
                        .show()
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
        LaunchedEffect(authState) {
            if (authState is AuthState.Authenticated) {
                onLoginSuccess()
            } else if (authState is AuthState.Error) {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}