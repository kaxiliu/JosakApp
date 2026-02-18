package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold

@Composable
fun RecordatorioScreen(onBack: () -> Unit){
    var recordatorioMovil by remember { mutableStateOf(true) }
    var recordatorioEmail by remember { mutableStateOf(false) }
    var inteligenteEnabled by remember { mutableStateOf(true) }
    var horaRecordatorio by remember { mutableStateOf("08:30") }

    SettingsScaffold(title = "RECORDATORIOS", onBackClick = onBack) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "¿Dónde quieres recibir tus avisos?",
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column {
                    // Opción Teléfono (Notificación Push)
                    ListItem(
                        headlineContent = { Text("Notificación en el móvil") },
                        leadingContent = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF03A9F4))},
                        trailingContent = {
                            Switch(checked = recordatorioMovil, onCheckedChange = { recordatorioMovil = it })
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    // Opción Email
                    ListItem(
                        headlineContent = { Text("Correo electrónico") },
                        leadingContent = {Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF03A9F4))},
                        trailingContent = {
                            Switch(checked = recordatorioEmail, onCheckedChange = { recordatorioEmail = it })
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hora de recordatorio diaria",
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Aquí abrirías el TimePicker */ },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hora establecida", fontWeight = FontWeight.Bold)
                    Text(
                        text = horaRecordatorio,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF03A9F4)
                    )
                }
            }
        }
    }
}