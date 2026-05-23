package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@Composable
fun BuscarAmigosScreen(
    userViewModel: UserViewModel,
    miId: String,
    onBack: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val usuarios by userViewModel.usuariosEncontrados.collectAsState()
    val followedNames by userViewModel.followedFriendNames.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadFollowedFriends()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Buscador
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                userViewModel.buscarUsuarios(it)
            },
            label = { Text("Buscar por nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (searchText.isEmpty()) {
            Text("Escribe un nombre para empezar a buscar...", modifier = Modifier.padding(16.dp))
        } else if (usuarios.isEmpty()) {
            Text(
                "No se encontró a ningún usuario llamado '$searchText'",
                modifier = Modifier.padding(16.dp),
                color = Color.Red
            )
        } else {
            LazyColumn {
                items(usuarios) { usuarioEncontrado ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nombre del usuario
                            Text(
                                text = usuarioEncontrado.nombre_usuario,
                                modifier = Modifier.weight(1f)
                            )

                            val isFollowing = followedNames.contains(usuarioEncontrado.nombre_usuario)
                            Button(
                                onClick = {
                                    userViewModel.followTargetUser(miId, usuarioEncontrado.uid, usuarioEncontrado.nombre_usuario)
                                },
                                enabled = !isFollowing,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFollowing) Color(0xFF4CAF50) else Color(0xFF03A9F4)
                                )
                            ) {
                                Text(if (isFollowing) "Siguiendo" else "Seguir")
                            }
                        }
                    }
                }
            }
        }
    }
}