package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlin.math.roundToInt
import edu.josakapp.proyectoJosakapp.R


@Composable
fun DraggablePenguin(modifier: Modifier = Modifier,
                     message: String? = null) {
    // Estados para recordar la posición X e Y del pingüino
    var offsetX by remember { mutableFloatStateOf(100f) } // Posición inicial X (opcional)
    var offsetY by remember { mutableFloatStateOf(500f) } // Posición inicial Y (opcional)
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
        if (!message.isNullOrEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                Text(
                    text = message,
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

    Image(
        painter = painterResource(id = R.drawable.pinguino), // Usando un placeholder por ahora
        contentDescription = "Pingüino interactivo",
        modifier = modifier
            .size(230.dp) // Tamaño del pingüino
            /*
            // Aplicar el desplazamiento basado en el estado actual
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            //  Detectar el gesto de arrastre
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // Consumir el evento para que no se propague
                    // Actualizar la posición sumando la cantidad arrastrada
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }*/
        )
    }
}