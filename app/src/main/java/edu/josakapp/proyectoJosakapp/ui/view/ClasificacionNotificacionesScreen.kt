package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold

@Composable
fun ClasificacionNotificacionesScreen(onBack: () -> Unit) {
    var avisarAdelantamientos by remember { mutableStateOf(true) }
    var avisarLigas by remember { mutableStateOf(true) }
    var resumenSemanal by remember { mutableStateOf(false) }

    SettingsScaffold(title = "CLASIFICACIÓN", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Controla los avisos de competición",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column {
                    // Opción 1: Adelantamientos
                    ListItem(
                        headlineContent = { Text("Adelantamientos", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Avisarme si alguien me supera en puntos.") },
                        trailingContent = {
                            Switch(checked = avisarAdelantamientos, onCheckedChange = { avisarAdelantamientos = it })
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    // Opción 2: Ligas
                    ListItem(
                        headlineContent = { Text("Estado de Liga", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Alertas de ascenso o descenso de categoría.") },
                        trailingContent = {
                            Switch(checked = avisarLigas, onCheckedChange = { avisarLigas = it })
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    // Opción 3: Resumen
                    ListItem(
                        headlineContent = { Text("Resultados finales", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Recibir el resumen de mi posición al final de la semana.") },
                        trailingContent = {
                            Switch(checked = resumenSemanal, onCheckedChange = { resumenSemanal = it })
                        }
                    )
                }
            }
        }
    }
}