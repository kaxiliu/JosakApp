package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edu.josakapp.proyectoJosakapp.data.model.Habito

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnyadirHabito(
    userId: Int,
    habitoInicial: Habito? = null,// Parámetro nuevo para edición
    totalCompletado: Int = 0, //Calculado el veces que se ha completado el habito
    onDismiss: () -> Unit,
    onConfirm: (Habito) -> Unit
) {
    // Inicializar estados con los valores del hábito si existe
    var habitName by remember { mutableStateOf(habitoInicial?.nombre ?: "") }
    var habitDescription by remember { mutableStateOf(habitoInicial?.descripcion ?: "") }

    // Para el color, convertimos el Long guardado de vuelta a un objeto Color
    var selectedColor by remember {
        mutableStateOf(if (habitoInicial != null) Color(habitoInicial.colorHex)
        else Color(0xFFE8F5E9))
    }
    var selectedIcon by remember { mutableStateOf(habitoInicial?.icono ?: "📚") }
    val scrollState = rememberScrollState()
    val listaIconos = listOf("💧", "🏃", "📚", "🍎", "🧘", "🎸", "🧹", "💊", "💻", "🛌", "🌱", "🍳")

    val options = listOf("Cada hora", "Todos los días", "Todas las semanas", "Todos los meses")
    var expanded by remember { mutableStateOf(false) }
    // Cargamos la frecuencia guardada o la primera por defecto
    var selectedOption by remember { mutableStateOf(habitoInicial?.frecuencia ?: options[0]) }

    // Asignamos XP según la frecuencia seleccionada,
    // usando remember para evitar recalcularlo innecesariamente
    val xpValue = remember(selectedOption) {
        when (selectedOption) {
            "Cada hora" -> 5
            "Todos los días" -> 10
            "Todas las semanas" -> 30
            "Todos los meses" -> 50
            else -> 10
        }
    }
    // Estado para mostrar/ocultar el pequeño mensaje de estadísticas
    // (Nuevo estado para el tooltip de información)
    var mostrarEstadisticas by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
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
                    // Cambia el título según el modo
                    Text(
                        text = if (habitoInicial == null) "Añadir un hábito" else "Editar hábito",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row {
                        IconButton(onClick = {
                            if (habitName.isNotBlank()) {

                                val colorVal = when(selectedColor) {
                                    Color(0xFFFFCDD2) -> 0xFFFFCDD2
                                    Color(0xFFBBDEFB) -> 0xFFBBDEFB
                                    Color(0xFFFFF9C4) -> 0xFFFFF9C4
                                    else -> 0xFFE8F5E9
                                }

                                // Crear o copiar el hábito preservando el ID si es edición
                                val habitoFinal = if (habitoInicial == null) {
                                    Habito(
                                        nombre = habitName,
                                        descripcion = habitDescription,
                                        exp_habito = xpValue, // Asignamos el XP calculado según la frecuencia
                                        frecuencia = selectedOption,
                                        estado = false,
                                        fecha_creacion = System.currentTimeMillis(),
                                        icono = selectedIcon,
                                        id_usuario = userId,
                                        colorHex=colorVal
                                    )
                                }else{
                                    // Si estamos editando, mantenemos el XP original
                                    // si la frecuencia no ha cambiado
                                    val finalXp = if (habitoInicial.frecuencia == selectedOption) {
                                        habitoInicial.exp_habito
                                    } else {
                                        xpValue
                                    }
                                    habitoInicial.copy(
                                        nombre = habitName,
                                        descripcion = habitDescription,
                                        exp_habito = finalXp,
                                        frecuencia = selectedOption,
                                        icono = selectedIcon,
                                        colorHex = colorVal
                                        // No tocamos id_habito, estado ni exp_habito para no perder el progreso
                                    )
                                }
                                onConfirm(habitoFinal)
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Confirmar")
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }
                }

                Column(modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()) {
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

                    TextField(
                        value = habitDescription,
                        onValueChange = { habitDescription = it },
                        placeholder = { Text("Descripción (opcional)...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SuggestionChip(onClick = { habitName = "Beber agua" }, label = { Text("Beber agua") })
                        SuggestionChip(onClick = { habitName = "Correr" }, label = { Text("Correr") })
                        SuggestionChip(onClick = { habitName = "Levantarse temprano" }, label = { Text("Levantarse temprano") })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Icono", fontSize = 14.sp, color = Color.Gray)
                    // Añadimos horizontalScroll(scrollState) para permitir el deslizamiento
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .horizontalScroll(scrollState), // Habilita el scroll
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listaIconos.forEach { icon ->
                            Box(
                                modifier = Modifier
                                    .size(45.dp) // Un poco más grande para facilitar el toque
                                    .background(
                                        if (selectedIcon == icon) Color(0xFF64B5F6)
                                        else Color.LightGray.copy(alpha = 0.3f),
                                        CircleShape
                                    )
                                    .clickable { selectedIcon = icon },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(icon, fontSize = 22.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Color", fontSize = 14.sp, color = Color.Gray)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFFFCDD2), CircleShape).clickable { selectedColor = Color(0xFFFFCDD2) })
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFBBDEFB), CircleShape).clickable { selectedColor = Color(0xFFBBDEFB) })
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFFFF9C4), CircleShape).clickable { selectedColor = Color(0xFFFFF9C4) })
                        Box(modifier = Modifier.size(24.dp).background(Color(0xFFE8F5E9), CircleShape).clickable { selectedColor = Color(0xFFE8F5E9) })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "Frecuencia:", color = Color(0xFF64B5F6))

                        // El Box actúa como ancla para el menú
                        Box {
                            Text(
                                text = selectedOption,
                                color = Color.Gray,
                                modifier = Modifier.clickable { expanded = true }
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                // Opcional: Puedes ajustar el offset si quieres separarlo un poco más
                                offset = DpOffset(x = 0.dp, y = 4.dp)
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
                        }
                    }

                    // Mostrar el icono de información solo si estamos editando
                    // (Muestra cuántas veces se ha completado el hábito)
                    if (habitoInicial != null) {
                        Box(contentAlignment = Alignment.CenterEnd) {
                            IconButton(
                                onClick = { mostrarEstadisticas = !mostrarEstadisticas },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Estadísticas",
                                    tint = Color(0xFF64B5F6),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Pequeño Tooltip/Mensaje flotante para mostrar los datos
                            if (mostrarEstadisticas) {
                                Surface(
                                    modifier = Modifier
                                        .padding(top = 30.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF424242),
                                    shadowElevation = 6.dp
                                ) {
                                    Text(
                                        text = "Completado: $totalCompletado veces",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}