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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.ui.components.AnyadirHabito
import edu.josakapp.proyectoJosakapp.ui.components.CalendarCard
import edu.josakapp.proyectoJosakapp.ui.components.DraggablePenguin
import edu.josakapp.proyectoJosakapp.ui.components.HabitoCard
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitoScreen(viewModel: HabitosViewModel, userId: Int,navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }// Abrir el añadir un habito
    var showDeleteDialog by remember { mutableStateOf(false) }//Borrar el habito
    val listaHabitos by viewModel.habitos.collectAsState()
    val registros by viewModel.todosLosRegistros.collectAsState()

    // Nuevo estado para rastrear qué hábito estamos editando
    var habitoSeleccionado by remember { mutableStateOf<Habito?>(null) }

    //deshacer el borrar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Controla si el menú está abierto o cerrado
    var menuExpanded by remember { mutableStateOf(false) }
    // Controla si el pingüino es visible o no
    var showPenguin by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadHabitos(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        //Imagen de fondo que cubre TODA la pantalla
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa de oscurecimiento
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)))

    Scaffold(containerColor = Color.Transparent,
             snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White,
                                    modifier = Modifier.size(25.dp)
                                )
                            }

                            // El menú desplegable
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(if (showPenguin) "Ocultar pingüino" else "Mostrar pingüino")
                                    },
                                    onClick = {
                                        showPenguin = !showPenguin // Cambia el estado
                                        menuExpanded = false // Cierra el menú
                                    }
                                )
                            }
                        }
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Add,
                                "Add",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp))
                        }
                        IconButton(onClick = { navController.navigate("stats")
                        }) {
                            Icon(ImageVector.vectorResource(id = R.drawable.fire),
                                "Streak",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp))
                        }
                        IconButton(onClick = { /* Money */ }) {
                            Icon(ImageVector.vectorResource(id = R.drawable.money),
                                "Money",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp))
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                CalendarCard(registros = registros)

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
                                //viewModel.updateEstado(habit.id_habito, !habit.estado)
                                viewModel.toggleHabito(habit)
                            },
                            onEdit = {h ->
                                // Clic en la tarjeta: Prepara el diálogo de edición
                                habitoSeleccionado = h
                                showDialog = true},
                            onLongClick = { h ->
                                habitoSeleccionado = h
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
        if (showPenguin) {
            DraggablePenguin(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 50.dp, end = 10.dp)
            )
        }

        if (showDialog) {
            AnyadirHabito(
                userId = userId,
                habitoInicial = habitoSeleccionado,
                onDismiss = {
                    showDialog = false
                    habitoSeleccionado = null
                },
                onConfirm = { h ->
                    if (habitoSeleccionado == null) viewModel.saveHabito(h)
                    else viewModel.updateHabito(h) // Actualizar existente
                    showDialog = false
                    habitoSeleccionado = null
                }
            )
        }

        if (showDeleteDialog && habitoSeleccionado != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("¿Eliminar hábito?", fontWeight = FontWeight.Bold) },
                text = { Text("¿Estás seguro de que quieres eliminar \"${habitoSeleccionado?.nombre}\"? ") },
                confirmButton = {
                    TextButton(onClick = {
                        val habitToDelete = habitoSeleccionado!!
                        viewModel.deleteHabito(habitToDelete)
                        showDeleteDialog = false


                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Habito eliminado",
                                actionLabel = "Deshacer",
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.saveHabito(habitToDelete)
                            }
                        }
                    }) {
                        Text("Eliminar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
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
