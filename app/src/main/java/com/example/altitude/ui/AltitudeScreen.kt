package com.example.altitude.ui

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AltitudeScreen(
    modifier: Modifier = Modifier,
    altitude: String = "-- m",
    speed: String = "SPEED -- km/h",
    lat: String = "LAT --°",
    lon: String = "LON --°",
    azimuth: Float = 0f // Added compass rotation angle
) {
    // Requirement 1 & 2: Lighter red and background adjustments
    val sunriseOrange = Color(0xFFFF9800)
    val lightRed = Color(0xFFEF5350)       // Much lighter than before
    val bottomRed = Color(0xFFE53935)      // Slightly deeper, but still light

    val lightTeal = Color(0xFF4DB6AC)

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(lightTeal, bottomRed)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        // Requirement 2: Exact sizing to guarantee a visual gap
        val innerCircleSize = 260.dp
        // Increased outer size to 320dp to make space for larger triangles + gap
        val totalDialSize = 320.dp

        // -----------------------------------------------------------
        // Outer White Ring & Compass Arrows (Rotates with phone)
        // -----------------------------------------------------------
        Box(
            modifier = Modifier
                .size(totalDialSize)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
                .rotate(-azimuth), // ROTATE THE ENTIRE RING based on sensor data
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

                // Gap Calculation:
                // Outer radius = 160dp, Inner radius = 130dp. Band width = 30dp.
                // Inner circle starts at Y = 30.dp from the top.
                // We put the triangle base at Y = 26.dp, leaving a 4.dp gap!

                // --- Paint configuration for text 'N' and 'S' ---
                val textPaintN = Paint().apply {
                    color = android.graphics.Color.WHITE // White text on Red triangle
                    textSize = 40f
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                }

                val textPaintS = Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 40f
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                }

                // --- North Triangle (Red) ---
                val northPath = Path().apply {
                    moveTo(center.x, 4.dp.toPx()) // Tip
                    lineTo(center.x - 16.dp.toPx(), 26.dp.toPx()) // Base Left
                    lineTo(center.x + 16.dp.toPx(), 26.dp.toPx()) // Base Right
                    close()
                }
                drawPath(path = northPath, color = Color.Red)
                // Draw 'N' inside the triangle (adjusted Y for baseline)
                drawContext.canvas.nativeCanvas.drawText("N", center.x, 22.dp.toPx(), textPaintN)

                // --- South Triangle (White) ---
                val southPath = Path().apply {
                    moveTo(center.x, canvasHeight - 4.dp.toPx()) // Tip
                    lineTo(center.x - 16.dp.toPx(), canvasHeight - 26.dp.toPx()) // Base Left
                    lineTo(center.x + 16.dp.toPx(), canvasHeight - 26.dp.toPx()) // Base Right
                    close()
                }
                drawPath(path = southPath, color = Color.White.copy(alpha = 0.3f))
                // Draw 'S' inside the triangle
                drawContext.canvas.nativeCanvas.drawText("S", center.x, canvasHeight - 10.dp.toPx(), textPaintS)
            }
        }

        // -----------------------------------------------------------
        // Inner Static Red Circle (Does not rotate, stays upright)
        // -----------------------------------------------------------
        val innerCircleGradient = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to sunriseOrange,
                0.3f to lightRed,
                1.0f to bottomRed
            )
        )

        Box(
            modifier = Modifier
                .size(innerCircleSize)
                .clip(CircleShape)
                .background(innerCircleGradient),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "CURRENT ALTITUDE",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = altitude,
                    color = Color.White,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = speed,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Bottom Coordinates
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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