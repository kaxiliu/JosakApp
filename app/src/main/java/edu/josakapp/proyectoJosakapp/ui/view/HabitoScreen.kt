package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.ui.components.AnyadirHabito
import edu.josakapp.proyectoJosakapp.ui.components.CalendarCard
import edu.josakapp.proyectoJosakapp.ui.components.HabitoCard
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitoScreen(viewModel: HabitosViewModel, userId: Int) {
    var showDialog by remember { mutableStateOf(false) }// Abrir el añadir un habito
    val listaHabitos by viewModel.habitos.collectAsState()


    LaunchedEffect(userId) {
        viewModel.loadHabitos(userId)
    }

    Scaffold(
        topBar = { TopAppBar(title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { /* Menu */ }) {
                            Icon(Icons.Default.Menu,
                                "Menu",
                                tint = Color.White)
                        }
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Add,
                                "Add",
                                tint = Color.White)
                        }
                        IconButton(onClick = { /* Stats */ }) {
                            Icon(ImageVector.vectorResource(id = R.drawable.fire),
                                "Streak",
                                tint = Color.White)
                        }
                        IconButton(onClick = { /* Money */ }) {
                            Icon(ImageVector.vectorResource(id = R.drawable.money),
                                "Money",
                                tint = Color.White)
                        }
                    }
                },
            windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                CalendarCard(habitos = listaHabitos)

                Spacer(modifier = Modifier.height(5.dp))


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(listaHabitos) { habit ->
                        HabitoCard(
                            habito = habit,
                            onClick = {
                                viewModel.updateEstado(habit.id_habito, !habit.estado)
                            },
                            onLongClick = { /**/}
                        )
                    }
                }
            }
        }


        if (showDialog) {
            AnyadirHabito(
                userId = userId,
                onDismiss = { showDialog = false },
                onConfirm = { nuevoHabito ->
                    viewModel.saveHabito(nuevoHabito)
                    showDialog = false
                }
            )
        }

    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoSeleccionHora() {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState() // Hora actual del sistema.
    var horaSeleccionada by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { mostrarDialogo = true }) {
            Text("Seleccionar hora")
        }

        Spacer(Modifier.height(8.dp))
        Text(text = "Hora seleccionada: $horaSeleccionada")

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                confirmButton = {
                    TextButton(onClick = {
                        val h = timePickerState.hour.toString().padStart(2, '0')
                        val m = timePickerState.minute.toString().padStart(2, '0')
                        horaSeleccionada = "$h:$m"
                        mostrarDialogo = false
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Selecciona la hora") },
                text = { TimePicker(state = timePickerState) }
            )
        }
    }
}

 */
