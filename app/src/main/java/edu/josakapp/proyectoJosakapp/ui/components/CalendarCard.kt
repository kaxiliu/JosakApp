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
import androidx.compose.ui.unit.dp
import edu.josakapp.proyectoJosakapp.data.model.Calendar


@Composable
fun CalendarCard() {
    //Para prueba ...TODO ViewModel
    val days = listOf(
        Calendar(19, "LUN", false),
        Calendar(20, "MAR", false),
        Calendar(21, "MIER", true),
        Calendar(22, "JUE", false),
        Calendar(23, "VIER", false),
        Calendar(24, "SAB", false),
        Calendar(25, "DOM", false)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                .size(width = 45.dp, height = 65.dp)
                .clip(RoundedCornerShape(8.dp))
                // Si termina un habito a ese dia ,el calendario va cambiar otro color
                .background(if (day.isDone) Color.Cyan.copy(alpha = 0.3f) else Color.LightGray.copy(alpha = 0.3f))
                .clickable { /*  */ },
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = day.dayName )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${day.day}",
                    color = if (day.isDone) Color.White else Color.Black
                )
            }
        }
    }
}
