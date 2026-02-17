package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.viewmodel.ThemeViewModel

@Composable
fun PreferenciasScreen(onBack: () -> Unit,
                       themeViewModel: ThemeViewModel
) {
    val isDarkModeGlobal = themeViewModel.isDarkMode

    SettingsScaffold(title = "PREFERENCIAS", onBackClick = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkModeGlobal) Color(0xFF1E1E1E) else Color.White
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "TEMA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isDarkModeGlobal) Color.White else Color.Black
                    )
                    Button(
                        onClick = { themeViewModel.toggleTheme() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkModeGlobal) Color(0xFFAECBFA) else Color(0xFF373737),
                            contentColor = if (isDarkModeGlobal) Color.Black else Color.White
                        ),
                        modifier = Modifier.height(45.dp)
                    ) {
                        Text(
                            text = if (isDarkModeGlobal) "Claro" else "Oscuro",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


