package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import edu.josakapp.proyectoJosakapp.R


@Composable
fun DraggablePenguin(modifier: Modifier = Modifier) {
    // Estados para recordar la posición X e Y del pingüino
    var offsetX by remember { mutableFloatStateOf(100f) } // Posición inicial X (opcional)
    var offsetY by remember { mutableFloatStateOf(200f) } // Posición inicial Y (opcional)

    Image(
        // ¡REEMPLAZA ESTO CON TU RECURSO DE IMAGEN!
        painter = painterResource(id = R.drawable.pinguino), // Usando un placeholder por ahora
        contentDescription = "Pingüino interactivo",
        modifier = modifier
            .size(230.dp) // Tamaño del pingüino
            // 1. Aplicar el desplazamiento basado en el estado actual
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            // 2. Detectar el gesto de arrastre
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // Consumir el evento para que no se propague
                    // Actualizar la posición sumando la cantidad arrastrada
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    )
}