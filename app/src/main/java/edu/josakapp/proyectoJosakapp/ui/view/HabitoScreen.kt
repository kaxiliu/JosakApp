package edu.josakapp.proyectoJosakapp.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edu.josakapp.proyectoJosakapp.R

/*
@Composable
fun HabitoScreen() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "josakapp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
            Text(
                text = "contenido",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
*/
@Composable
fun HabitoScreen() {
    var showDialog by remember { mutableStateOf(false) }// Abrir el añadir un habito

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "josakapp",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        /*
            Text(
                text = "contenido",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { showDialog = true }
            )*/
        }

        if (showDialog) {
            AnyadirHabito(
                onDismiss = { showDialog = false },
                onConfirm = { showDialog = false }
            )
        }
    }
}

@Composable
fun AnyadirHabito(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var habitName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(0xFFE0E0E0)) }

    val options = listOf("Todos los días", "Todas las semanas", "Todos los meses")// 3 opciones para seleccionar
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(selectedColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Añadir un hábito", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Row {
                        IconButton(onClick = {  onConfirm(habitName) }) {
                            Icon(Icons.Default.Check, contentDescription = "Confirmar")
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = habitName,
                        onValueChange = { habitName = it },
                        placeholder = { Text("Escribe el nombre del hábito...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    //cambiar fila autonamente
                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SuggestionChip(onClick = { habitName = "Beber agua" }, label = { Text("Beber agua") })
                        SuggestionChip(onClick = { habitName = "Correr" }, label = { Text("Correr") })
                        SuggestionChip(onClick = { habitName = "Levantarse temprano" }, label = { Text("Levantarse temprano") })
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = "Color", fontSize = 14.sp, color = Color.Gray)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(24.dp).background( Color(0xFFFFCDD2), CircleShape).clickable { selectedColor = Color(0xFFFFCDD2)})
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFBBDEFB), CircleShape).clickable { selectedColor = Color(0xFFBBDEFB) } )
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFFFF9C4), CircleShape).clickable { selectedColor =Color(0xFFFFF9C4)})
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFE0E0E0), CircleShape).clickable { selectedColor =Color(0xFFE0E0E0)})
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                        Text(text = "Recuérdamelo:", color = Color(0xFF64B5F6))
                        Text(
                            text = " $selectedOption",
                            color = Color.Gray,
                            modifier = Modifier
                                .clickable { expanded = true }
                            )
                    }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                    }
                                )
                        }
                    }
                    DialogoSeleccionHora()
                }
            }
        }
    }
}


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

