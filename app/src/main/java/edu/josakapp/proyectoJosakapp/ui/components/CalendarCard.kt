package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                 contentPadding: androidx.compose.foundation.layout.PaddingValues =
    androidx.compose.foundation.layout.PaddingValues(16.dp)) {
    // Obtener la fecha actual del sistema
    val today = LocalDate.now()

    // Obtener el lunes de la semana actual
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    // Generar los datos de los 7 días de la semana actual
    val days = (0..6).map { i ->
        val date = monday.plusDays(i.toLong())
        val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val hasRecord = registros.any { it.fecha == dateMillis }


        Calendar(
            day = date.dayOfMonth,
            dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es")).uppercase(),
            // El color cambia si es el día actual y hay tareas completadas
            isDone = hasRecord
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { day ->
            DayItem(day)
        }
    }
}


@Composable
fun DayItem(day: Calendar) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 50.dp)
                .clip(RoundedCornerShape(8.dp))
                // Si termina un habito a ese dia ,el calendario va cambiar otro color
                .background(if (day.isDone) Color(0xFF90EE90).copy(alpha = 0.8f)
                else Color.White.copy(alpha = 0.8f))
                .clickable { /*  */ },
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = day.dayName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (day.isDone) Color.White else Color.Black)

                Text(
                    text = "${day.day}",
                    fontSize = 12.sp,
                    color = if (day.isDone) Color.White else Color.Black
                )
            }
        }
    }
}
