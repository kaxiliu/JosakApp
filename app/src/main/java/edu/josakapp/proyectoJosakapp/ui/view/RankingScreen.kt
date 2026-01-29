package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModel

@Composable
fun RankingScreen(viewModel: RankingViewModel = RankingViewModel()) {

    val ranking by viewModel.ranking.collectAsState()

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

            Spacer(modifier = Modifier.height(20.dp))

            // Lista del ranking
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(ranking) { user ->
                    RankingItem(
                        user,
                        position = ranking.indexOf(user) + 1
                    )
                }
            }
        }
    }
}

@Composable
fun RankingItem(user: UserRanking, position: Int) {

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
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // MEDALLA O NÚMERO
            Text(
                text = medalla,
                color = Color.Black,
                modifier = Modifier.padding(end = 12.dp)
            )

            // FOTO DE PERFIL
            Image(
                painter = rememberAsyncImagePainter(user.fotoPerfil),
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 12.dp)
            )

            // NOMBRE + PUNTOS + NIVEL
            Column {
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
        }
    }
}
