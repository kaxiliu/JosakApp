package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold


@Composable
fun PerfilScreen(
    user: User,
    onNavigateToSettings: () -> Unit,
    onCompleteProfile: () -> Unit // Nueva navegación para completar perfil
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. CABECERA (Foto y Ajustes) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Foto Central
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                shape = CircleShape,
                color = Color.LightGray.copy(alpha = 0.3f),
                border = BorderStroke(2.dp, Color(0xFF03A9F4))
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(20.dp))
            }

            // Tres puntos arriba a la derecha
            IconButton(
                onClick = { onNavigateToSettings() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "Ajustes")
            }
        }

        // --- 2. NOMBRE Y SEGUIDORES (Estilo IG) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = user.nombre_usuario ?: "Usuario",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("124", fontWeight = FontWeight.Bold) // Ejemplo
                    Text("Seguidores", fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("86", fontWeight = FontWeight.Bold) // Ejemplo
                    Text("Siguiendo", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // --- 3. BOTONES DE ACCIÓN (Añadir y Compartir) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Lógica añadir */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
            ) {
                Text("Añadir amigos")
            }

            IconButton(
                onClick = { /* Lógica compartir */ },
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            ) {
                Icon(Icons.Default.Share, contentDescription = "Compartir")
            }
        }

        // --- 4. CUADRO "COMPLETA TU PERFIL" ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable { onCompleteProfile() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Completa tu perfil", fontWeight = FontWeight.Bold)
                    Text("Añade una descripción y foto para que te reconozcan.", fontSize = 12.sp)
                }
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }

        // --- 5. RESUMEN (4 CARDS DE STATS) ---
        Text(
            text = "Resumen de actividad",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold
        )

        // Usamos un Grid de 2x2
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatSmallCard("Racha", "7 días", Icons.Default.Whatshot, Color(0xFFFF5722), Modifier.weight(1f))
                StatSmallCard("Exp Total", "2500 XP", Icons.Default.Bolt, Color(0xFFFFD700), Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatSmallCard("Rango", "Oro II", Icons.Default.EmojiEvents, Color(0xFF03A9F4), Modifier.weight(1f))
                StatSmallCard("Posición", "#12", Icons.Default.BarChart, Color(0xFF4CAF50), Modifier.weight(1f))
            }
        }

        // --- 6. LOGROS ---
        Text(
            text = "Mis Logros",
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )

        // Simulación de fila de logros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = Color.LightGray.copy(alpha = 0.2f)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.padding(15.dp), tint = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StatSmallCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}