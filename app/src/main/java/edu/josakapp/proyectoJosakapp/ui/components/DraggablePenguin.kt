package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.R
import kotlin.math.roundToInt

@Composable
fun DraggablePenguin(
    modifier: Modifier = Modifier,
    message: String? = null,
    nivelSed: Float? = null,
    accesorioResId: Int? = null
) {
    var offsetX by remember { mutableFloatStateOf(100f) }
    var offsetY by remember { mutableFloatStateOf(500f) }

    Column(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((-80).dp)
    ) {
        PenguinStatusHeader(mensaje = message, nivelSed = nivelSed)

        Box(modifier = Modifier.size(230.dp)) {
            Image(
                painter = painterResource(id = R.drawable.pinguino),
                contentDescription = "Pingüino interactivo",
                modifier = Modifier.fillMaxSize()
            )

            if (accesorioResId != null && accesorioResId != 0) {
                Image(
                    painter = painterResource(id = accesorioResId),
                    contentDescription = "Accesorio equipado",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(120.dp)
                        .offset(y = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PenguinStatusHeader(
    mensaje: String? = null,
    nivelSed: Float? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        if (nivelSed != null) {
            Column(
                modifier = Modifier.width(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sed",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(5.dp))
                        .padding(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(nivelSed)
                            .fillMaxHeight()
                            .background(Color(0xFF2196F3), RoundedCornerShape(4.dp))
                    )
                }
            }
        } else if (!mensaje.isNullOrEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = mensaje,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .offset(y = (-5).dp)
                        .background(Color.White, RoundedCornerShape(1.dp))
                )
            }
        }
    }
}