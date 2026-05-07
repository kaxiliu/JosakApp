package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import kotlin.math.roundToInt
import edu.josakapp.proyectoJosakapp.R


@Composable
fun DraggablePenguin(modifier: Modifier = Modifier,
                     message: String? = null,
                     nivelSed: Float? = null) {
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
        PenguinStatusHeader(mensaje = message, nivelSed = nivelSed)
//        if (!message.isNullOrEmpty()) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Box(
//                    modifier = Modifier
//                        .background(Color.White, RoundedCornerShape(12.dp))
//                        .padding(horizontal = 10.dp, vertical = 6.dp)
//                ) {
//                Text(
//                    text = message,
//                    color = Color.Black,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .size(10.dp)
//                    .offset(y = (-5).dp)
//                    .background(Color.White, RoundedCornerShape(1.dp))
//                )
//            }
//        }

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


@Composable
fun PenguinStatusHeader(
    mensaje: String? = null,
    nivelSed: Float? = null // Si es null, no mostramos la barra
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        if (nivelSed != null) {
            // --- MOSTRAR BARRA DE SED
            Column(
                modifier = Modifier.width(120.dp), // Ancho fijo para que quede bien sobre la cabeza
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
                // Triangulito del mensaje
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