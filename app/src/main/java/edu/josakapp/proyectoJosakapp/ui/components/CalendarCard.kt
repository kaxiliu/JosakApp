package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.data.model.Calendar
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun CalendarCard(registros: List<HabitoRegistro>,
                 modifier: Modifier = Modifier,
                 puedeExpandirse: Boolean = true) {

    var isExpanded by remember { mutableStateOf(if (puedeExpandirse) false else false) }
    var fechaFocal by remember { mutableStateOf(LocalDate.now()) }

    Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateContentSize(), // Empuja el contenido inferior suavemente
            //shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ){
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (puedeExpandirse) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isExpanded) {
                            IconButton(onClick = { fechaFocal = fechaFocal.minusMonths(1) }) {
                                Icon(
                                    Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Mes Anterior"
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(24.dp))
                        }

                        Text(
                            text = "${
                                fechaFocal.month.getDisplayName(TextStyle.FULL, Locale("es"))
                                    .uppercase()
                            } ${fechaFocal.year}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (isExpanded) {
                            IconButton(onClick = { fechaFocal = fechaFocal.plusMonths(1) }) {
                                Icon(
                                    Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Mes Siguiente"
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("L", "M", "X", "J", "V", "S", "D").forEach { letra ->
                        Text(
                            text = letra,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(36.dp), // Alineación con el DayItem
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // --- CONTENIDO DINÁMICO ---
                if (!isExpanded || !puedeExpandirse) {
                    // MODO SEMANAL
                    val hoy = LocalDate.now()
                    val lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (0..6).forEach { i ->
                            val date = lunes.plusDays(i.toLong())
                            DayItem(obtenerEstadoDia(date, registros))
                        }
                    }
                } else {
                    // MODO MENSUAL COMPLETO
                    VistaMensualCompleta(fechaFocal, registros)
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Si el calendario puede expandirse,
                // mostramos el botón para alternar entre vista semanal y mensual
                if (puedeExpandirse) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.height(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Cerrar" else "Abrir",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
}



@Composable
fun DayItem(day: Calendar) {
    Box(
            modifier = Modifier
                .size(36.dp, )
                .clip(RoundedCornerShape(8.dp))
                // Si termina un habito a ese dia ,el calendario va cambiar otro color
                .background(if (day.isDone) Color(0xFF90EE90).copy(alpha = 0.8f) else Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ){

                Text(
                    text = "${day.day}",
                    fontSize = 12.sp,
                    color = if (day.isDone) Color.White else Color.Black
                )
            }
}


@Composable
private fun VistaMensualCompleta(fechaFocal: LocalDate, registros: List<HabitoRegistro>) {
    val primerDiaMes = fechaFocal.withDayOfMonth(1)
    val diasEnMes = fechaFocal.lengthOfMonth()
    val desplazamiento = primerDiaMes.dayOfWeek.value - 1
    val totalCeldas = diasEnMes + desplazamiento
    val filas = (totalCeldas + 6) / 7

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (f in 0 until filas) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (c in 0 until 7) {
                    val indice = f * 7 + c
                    val numDia = indice - desplazamiento + 1

                    if (numDia in 1..diasEnMes) {
                        val fechaPos = fechaFocal.withDayOfMonth(numDia)
                        DayItem(obtenerEstadoDia(fechaPos, registros))
                    } else {
                        // Espacio vacío para mantener la cuadrícula
                        Spacer(modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}
private fun obtenerEstadoDia(fecha: LocalDate, registros: List<HabitoRegistro>): Calendar {
    val fechaEnMilis = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val estaCompletado = registros.any { it.fecha == fechaEnMilis }
    val nombreDia = fecha.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale("es"))
    return Calendar(day = fecha.dayOfMonth, dayName = nombreDia, isDone = estaCompletado)
}
