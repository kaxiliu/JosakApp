package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.josakapp.proyectoJosakapp.data.model.Habito

@Composable
fun HabitoCard(
    habito: Habito,
    onClick: (Habito) -> Unit = {}, //Complir
    onLongClick: (Habito) -> Unit = {} //Modificar
) {
    // Muestra la información
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(habito) },
                onLongClick = { onLongClick(habito) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Nombre de habito
            Text(
                text = habito.nombre,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // procentaje
            Text( text = habito.estado.toString())
        }
    }
}



