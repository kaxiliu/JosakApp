package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
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
    onDismiss: () -> Unit,
    onConfirm: (Habito) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color(0xFFE8F5E9)) }
    var selectedIcon by remember { mutableStateOf("📚") }
    val options = listOf("Todos los días", "Todas las semanas", "Todos los meses")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

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
                    Text(text = "Añadir un hábito", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Row {
                        IconButton(onClick = {
                            if (habitName.isNotBlank()) {
                                val newHabito = Habito(
                                    nombre = habitName,
                                    descripcion = habitDescription,
                                    exp_habito = 0,
                                    frecuencia = selectedOption,
                                    estado = false,
                                    fecha_creacion = System.currentTimeMillis(),
                                    icono = selectedIcon,
                                    id_usuario = userId,
                                    colorHex = when(selectedColor) {
                                        Color(0xFFFFCDD2) -> 0xFFFFCDD2
                                        Color(0xFFBBDEFB) -> 0xFFBBDEFB
                                        Color(0xFFFFF9C4) -> 0xFFFFF9C4
                                        else -> 0xFFE8F5E9
                                    }
                                )
                                onConfirm(newHabito)
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
                        listOf("💧", "🏃", "📚", "🍎").forEach { icon ->
                            Box(modifier = Modifier
                                .size(40.dp)
                                .background(if (selectedIcon == icon) Color(0xFF64B5F6) else Color.LightGray, CircleShape)
                                .clickable { selectedIcon = icon },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(icon, fontSize = 20.sp)
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
                }
            }
        }
    }
}
