package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel


/**Clase que se encargará de mostrar la pantalla de ajustes de la app*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen (onNavigate: (String) -> Unit,
                    onBack: () -> Unit,
                    userViewModel: UserViewModel = viewModel()){
    val colorCeleste = Color(0xFF03A9F4)
    SettingsScaffold(title = "AJUSTES", onBackClick = onBack){padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column {
                    SettingItem("Preferencias", Icons.Default.Settings) {
                        onNavigate(NavScreens.NavPreferenciasScreen.ruta)
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    SettingItem("Perfil", Icons.Default.Person) {
                        onNavigate(NavScreens.NavPerfilScreen.ruta)
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    SettingItem("Notificaciones", Icons.Default.Notifications) {
                        onNavigate(NavScreens.NavNotificacionesScreen.ruta)
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    SettingItem("Privacidad", Icons.Default.Lock) {
                        onNavigate(NavScreens.NavConfiguracionPrivacidadScreen.ruta)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                          userViewModel.logout()
                          onNavigate(NavScreens.NavMainScreen.ruta)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = colorCeleste
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(text = "CERRAR SESIÓN",
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun SettingItem(title: String,
                icon: ImageVector,
                onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title, fontSize = 16.sp) },
        leadingContent = { Icon(icon, contentDescription = null, tint = Color.Gray) },
        trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable { onClick() }
    )
}

