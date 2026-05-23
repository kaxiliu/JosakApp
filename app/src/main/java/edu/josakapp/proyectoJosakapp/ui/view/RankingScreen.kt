package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import edu.josakapp.proyectoJosakapp.converter.base64ToBitmap

@Composable
fun RankingScreen(viewModel: RankingViewModel, userViewModel: UserViewModel, navController: NavHostController) {

    val ranking by viewModel.ranking.collectAsState()
    val soloAmigos by viewModel.soloAmigos.collectAsState()
    val userState by userViewModel.user.collectAsState()
    val currentName = userState?.nombre_usuario ?: ""

    var query by remember { mutableStateOf("") }

    // Cargar ranking al entrar
    LaunchedEffect(Unit) {
        viewModel.loadRanking()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "josakapp",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {

            // Título
            Text(
                text = "Ranking de Usuarios",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Top actions: toggle friends + refresh + search
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (soloAmigos) "Ver ranking global" else "Ver ranking de amigos",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .clickable { viewModel.toggleRanking() }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.loadRanking() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Buscar usuario") })

            Spacer(modifier = Modifier.height(12.dp))

            // Mostrar posición del usuario actual si existe
            val currentIndex = ranking.indexOfFirst { it.nombre_usuario == currentName }
            if (currentIndex >= 0) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Tu posición: #${currentIndex + 1}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 12.dp))
                        Text("${ranking[currentIndex].puntos} pts · Nivel ${ranking[currentIndex].nivel}")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista del ranking (filtrada por búsqueda)
            val filtered = if (query.isBlank()) ranking else ranking.filter { it.nombre_usuario.contains(query, ignoreCase = true) }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(filtered) { index, user ->
                    val originalIndex = ranking.indexOfFirst { it.nombre_usuario == user.nombre_usuario }
                    val isCurrent = user.nombre_usuario == currentName
                    RankingItem(
                        user = user,
                        position = originalIndex + 1,
                        isCurrent = isCurrent,
                        onAddFriend = { /* deprecated */ },
                        onOpenProfile = {
                            // navegar a la pantalla de perfil del usuario
                            navController.navigate("perfil_user/${user.id_usuario}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RankingItem(user: UserRanking, position: Int, isCurrent: Boolean = false, onAddFriend: () -> Unit, onOpenProfile: () -> Unit) {

    // Colores según posición
    val backgroundColor = when (position) {
        1 -> Color(0xFFFFD700).copy(alpha = 0.85f) // Oro
        2 -> Color(0xFFC0C0C0).copy(alpha = 0.85f) // Plata
        3 -> Color(0xFFCD7F32).copy(alpha = 0.85f) // Bronce
        else -> Color.White.copy(alpha = 0.9f)
    }

    // Medallas
    val medalla = when (position) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "$position."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Color(0xFF90EE90).copy(alpha = 0.95f) else backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // MEDALLA O NÚMERO
            Text(
                text = medalla,
                color = Color.Black,
                modifier = Modifier.padding(end = 12.dp)
            )

            // FOTO DE PERFIL
            val bitmap = remember(user.fotoPerfil) { user.fotoPerfil.takeIf { it.isNotBlank() }?.let { base64ToBitmap(it) } }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .padding(end = 12.dp)
                )
            } else {
                AsyncImage(
                    model = R.drawable.ic_person_placeholder,
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 12.dp)
                )
            }

            // NOMBRE + PUNTOS + NIVEL
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.nombre_usuario,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${user.puntos} pts · Nivel ${user.nivel}",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // BOTÓN: ir al perfil
            IconButton(
                onClick = onOpenProfile
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Ver perfil",
                    tint = Color.Black
                )
            }
        }
    }
}
