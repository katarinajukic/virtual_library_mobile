package com.example.virtuallibrary.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virtuallibrary.ui.theme.DirtyWhite
import com.example.virtuallibrary.ui.theme.GreenColor
import com.example.virtuallibrary.ui.theme.robotoBold

@Composable
fun SearchScreen(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "Search for a book in our library",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(22.dp))

        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter a book title") },
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

        Button(
            onClick = {
                Log.d("SearchScreen", "Search button clicked with query: $query")
                onSearch(query)
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
                "SEARCH",
                style = TextStyle(
                    fontFamily = robotoBold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}