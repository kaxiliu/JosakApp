package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
fun SubMenuAmigosScreen(onBack: () -> Unit) {
    var seguirByMe by remember { mutableStateOf(true) }
    var logrosCompartidos by remember { mutableStateOf(true) }

    SettingsScaffold(title = "AMIGOS", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Configura cómo interactúas con tu red",
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
                    // Opción de Seguidores
                    ListItem(
                        headlineContent = { Text("Nuevos seguidores", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Avisarme cuando alguien me siga.") },
                        trailingContent = {
                            Switch(checked = seguirByMe, onCheckedChange = { seguirByMe = it })
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    ListItem(
                        headlineContent = { Text("Actividad de amigos", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Avisarme cuando un amigo complete un reto.") },
                        trailingContent = {
                            Switch(checked = logrosCompartidos, onCheckedChange = { logrosCompartidos = it })
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // LA OPCIÓN QUE TÚ PENSASTE: Invitar
            Text(
                text = "¿Quieres hacer crecer tu comunidad?",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { /* Lógica para compartir enlace */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("INVITAR A NUEVOS AMIGOS")
            }
        }
    }
}