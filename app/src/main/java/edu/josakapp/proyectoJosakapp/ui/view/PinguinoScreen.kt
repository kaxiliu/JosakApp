package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.ui.components.DraggablePenguin
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@Composable
fun PinguinoScreen(
    userViewModel: UserViewModel,
    habitosViewModel: HabitosViewModel,
    onBack: () -> Unit
) {
    // Obtenemos los datos para que el pingüino "hable"
    val listaHabitos by habitosViewModel.habitos.collectAsState()
    val registros by habitosViewModel.todosLosRegistros.collectAsState()
    val hoy = java.time.LocalDate.now()
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    var menuTareasExpandido by remember { mutableStateOf(false) }

    // Lógica de mensaje: Si faltan hábitos, pregunta por ellos
    val mensaje = remember(listaHabitos, registros) {
        val habitosPendientes = listaHabitos.find { habit ->
            registros.none { it.id_habito == habit.id_habito && it.fecha == hoy }
        }
        habitosPendientes?.let { "¿Has hecho ${it.nombre} hoy?" } ?: "¡Todo completado! Eres genial."
    }

    var nivelSed by remember { mutableFloatStateOf(0.6f) }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box {
                IconButton(
                    onClick = { menuTareasExpandido = true },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .size(45.dp)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.Black)
                }

                DropdownMenu(
                    expanded = menuTareasExpandido,
                    onDismissRequest = { menuTareasExpandido = false }
                ) {
                    DropdownMenuItem(text = { Text("Dar de beber") }, onClick = {
                        if(nivelSed < 1f) nivelSed += 0.1f
                        menuTareasExpandido = false
                    })
                    DropdownMenuItem(text = { Text("Cambiar ropa") }, onClick = { menuTareasExpandido = false })
                }
            }
            Spacer(modifier = Modifier.width(30.dp))


        }


        DraggablePenguin(
            nivelSed = nivelSed, // Muestra la barra azul
            message = null
        )
    }
}