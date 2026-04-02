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
fun AltitudeScreen(
    modifier: Modifier = Modifier,
    altitude: String = "1081 m",      // Added dynamic parameter
    speed: String = "SPEED 5 km/h",   // Added dynamic parameter
    lat: String = "LAT 26°36'7\" N",  // Added dynamic parameter
    lon: String = "LON 106°45'11\" E" // Added dynamic parameter
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4DB6AC),
            Color(0xFFD4A373)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
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

                // Use the variable here
                Text(
                    text = altitude,
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Use the variable here
                Text(text = speed, color = Color.White, fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use the variables here
            Text(text = lat, color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = lon, color = Color.White, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AltitudeScreenPreview() {
    AltitudeScreen()
}