package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold

@Composable
fun PrivacidadScreen(onBack: () -> Unit) {
    SettingsScaffold(title = "PRIVACIDAD", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()) // Permite hacer scroll si hay mucho texto
        ) {
            Text(
                text = "Configuración de Privacidad",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "En JosakApp nos tomamos muy en serio tus datos. Aquí puedes leer cómo gestionamos tu información:\n\n" +
                        "1. Tus datos de hábitos son privados y solo tú puedes verlos a menos que decidas compartirlos con amigos.\n\n" +
                        "2. No compartimos tu información personal con terceros para fines publicitarios.\n\n" +
                        "3. Puedes solicitar la eliminación de tu cuenta y todos tus datos en cualquier momento desde el soporte técnico.\n\n" +
                        "4. El ranking utiliza tu nombre de usuario para mostrar tu progreso a la comunidad.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Última actualización: Febrero 2024",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}