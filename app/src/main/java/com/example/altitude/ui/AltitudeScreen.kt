package com.example.altitude.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AltitudeScreen(modifier: Modifier = Modifier) {
    // 1. Vertical gradient background (Top: Teal, Bottom: Sand)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4DB6AC),
            Color(0xFFD4A373)
        )
    )

    // 2. Base full-screen background
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {

        // 3. Center red dial
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(Color(0xFFF44336)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "CURRENT ALTITUDE", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "1081 m",
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "SPEED 5 km/h", color = Color.White, fontSize = 14.sp)
            }
        }

        // 4. Bottom coordinate information
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "LAT 26°36'7\" N", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "LON 106°45'11\" E", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AltitudeScreenPreview() {
    AltitudeScreen()
}