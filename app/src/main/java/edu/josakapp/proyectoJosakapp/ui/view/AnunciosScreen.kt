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
fun AnunciosScreen(onBack: () -> Unit) {
    var avisarNovedades by remember { mutableStateOf(true) }
    var avisarOfertas by remember { mutableStateOf(false) }

    SettingsScaffold(title = "ANUNCIOS", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Mantente al día con JosakApp",
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
                    ListItem(
                        headlineContent = { Text("Nuevas funciones", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Sé el primero en probar las actualizaciones.") },
                        trailingContent = {
                            Switch(checked = avisarNovedades, onCheckedChange = { avisarNovedades = it })
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)
                    ListItem(
                        headlineContent = { Text("Ofertas y promociones", fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("Descuentos en la tienda y eventos especiales.") },
                        trailingContent = {
                            Switch(checked = avisarOfertas, onCheckedChange = { avisarOfertas = it })
                        }
                    )
                }
            }
        }
    }
}