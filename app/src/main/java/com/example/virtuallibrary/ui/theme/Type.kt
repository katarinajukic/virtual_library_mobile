package com.example.virtuallibrary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.virtuallibrary.R

val robotoBold = FontFamily(
    Font(R.font.robotobold, FontWeight.Bold)
)

val comfortaaLight = FontFamily(
    Font(R.font.comfortaalight, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = comfortaaLight,
        fontWeight = FontWeight.Light,
        fontSize = 36.sp,
        color = GreenColor
    )
)

@Composable
fun VirtualLibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography,
        content = content
    )
}