package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.util.formatHabitRemainingTime
import kotlinx.coroutines.delay

@Composable
fun HabitoCard(
    habito: Habito,
    lastCompletedAtMillis: Long? = null,
    onClick: (Habito) -> Unit = {}, //Estado
    onEdit: (Habito) -> Unit,//Editar
    onLongClick: (Habito) -> Unit = {} //Borrar
) {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(habito.id_habito, lastCompletedAtMillis, habito.frecuencia, habito.fecha_creacion) {
        currentTimeMillis = System.currentTimeMillis()
        while (true) {
            delay(60_000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    val timerText = remember(lastCompletedAtMillis, habito.fecha_creacion, habito.frecuencia, currentTimeMillis) {
        formatHabitRemainingTime(
            lastCompletedAt = lastCompletedAtMillis,
            createdAt = habito.fecha_creacion,
            frecuencia = habito.frecuencia,
            now = currentTimeMillis
        )
    }

    // Muestra la información
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEdit(habito) },// El clic en la tarjeta para editar
                onLongClick = { onLongClick(habito) }// El clic largo ahora es para borrar
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight()
                    .background(Color(habito.colorHex))
            )

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = habito.icono, fontSize = 28.sp, modifier = Modifier.width(40.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = habito.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(text = habito.frecuencia, fontSize = 12.sp, color = Color.Gray)
                        if (habito.estado) {
                            Text(
                                text = "Siguiente en $timerText",
                                fontSize = 11.sp,
                                color = Color(0xFF607D8B)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { onClick(habito) },
                    modifier = Modifier.size(32.dp)
                ) {
                    if (habito.estado) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completado",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Pendiente",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Descripcion
            if (habito.descripcion.isNotEmpty()) {
                Text(
                    text = habito.descripcion,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Progreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    progress = { if (habito.estado) 1f else 0f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    color = if (habito.estado) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    trackColor = if (habito.estado) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${habito.exp_habito} XP",
                    fontSize = 11.sp,
                    color = if (habito.estado) Color(0xFF4CAF50) else Color.Gray,
                    fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
