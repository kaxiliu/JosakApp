package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens

@Composable
fun NotificacionesScreen(onBack: () -> Unit,
                         onNavigate: (String) -> Unit) {
    SettingsScaffold(title = "NOTIFICACIONES", onBackClick = onBack) { padding ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column{
                    // RECORDATORIOS
                    NotificationClickableItem(
                        title = "Recordatorios",
                        subtitle = "Configura tus alarmas y avisos ",
                        onClick = {onNavigate(NavScreens.NavRecordatorioScreen.ruta)}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)
                    // AMIGOS
                    NotificationClickableItem(
                        title = "Amigos",
                        subtitle = "Gestiona notificaciones de interacción.",
                        onClick = { onNavigate(NavScreens.NavAmigosScreen.ruta) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    //  CLASIFICACIÓN
                    NotificationClickableItem(
                        title = "Clasificación",
                        subtitle = "Avisos sobre tu puesto en el ranking.",
                        onClick = { onNavigate(NavScreens.NavClasificacion.ruta) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 0.5.dp)

                    //  ANUNCIOS
                    NotificationClickableItem(
                        title = "Anuncios",
                        subtitle = "Recibe noticias y novedades.",
                        onClick = { /* Navegar a pantalla de anuncios */ }
                    )
                }
            }
        }
    }
}


/**Componente clickable*/
@Composable
fun NotificationClickableItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable{onClick()}
    )

}