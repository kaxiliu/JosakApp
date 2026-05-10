package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.ui.components.DraggablePenguin
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    // --- ESTADOS PARA LA INTERACCIÓN ---
    var showSheet by remember { mutableStateOf(false) } // Controla si el BottomSheet es visible
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Mochila, 1: Tienda
    var itemSeleccionado by remember { mutableStateOf<Int?>(null) } // Guarda el índice del item clicado
    var cantidad by remember { mutableIntStateOf(1) } // Cantidad para comprar o alimentar

    val titulosTabs = listOf("Mochila", "Tienda")
    val listaBebidas = listOf(R.drawable.bebida1, R.drawable.bebida2, R.drawable.bebida3, R.drawable.bebida4)
    // Define los precios correspondientes a cada bebida
    val preciosBebidas = mapOf(R.drawable.bebida1 to 5, R.drawable.bebida2 to 20,
        R.drawable.bebida3 to 15,R.drawable.bebida4 to 10
    )

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
                        showSheet = true
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

        // --- MODAL BOTTOM SHEET ACTUALIZADO ---
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(16.dp)
                ) {
                    // Solo el botón X alineado a la derecha
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = { showSheet = false },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    // TABS
                    TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.Transparent) {
                        titulosTabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = {
                                    selectedTabIndex = index
                                    itemSeleccionado = null
                                },
                                text = { Text(title) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CONTENIDO
                    Box(modifier = Modifier.weight(1f)) {
                        if (selectedTabIndex == 0) {
                            // MOCHILA principalmente vacía, pero con mensaje
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Mochila vacía", color = Color.Gray)
                            }
                        } else {
                            // --- TIENDA CON REJILLA FIJA ---
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val filas = listaBebidas.chunked(3)
                                items(filas) { fila ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        fila.forEach { resId ->
                                            // Obtenemos el precio del mapa, si no existe ponemos 0 por defecto
                                            val precio = preciosBebidas[resId] ?: 0

                                            ItemBebidaUI(
                                                resId = resId,
                                                esTienda = true,
                                                estaSeleccionado = itemSeleccionado == resId,
                                                precio = precio,
                                                onSelect = { itemSeleccionado = resId }
                                            )
                                        }
                                        // Espaciadores para mantener el tamaño del grid si la fila no está llena
                                        repeat(3 - fila.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // PANEL DE ACCIÓN
                    if (itemSeleccionado != null) {
                        ActionPanel(
                            esTienda = selectedTabIndex == 1,
                            cantidad = cantidad,
                            onCantidadChange = { cantidad = it },
                            onConfirm = { showSheet = false }
                        )
                    }
                }
            }
        }
    }
}

// Componente para cada bebida,
//cuando seleccionamos una bebida en la tienda,
// se resalta con un borde
@Composable
fun RowScope.ItemBebidaUI(
    resId: Int,
    esTienda: Boolean,
    estaSeleccionado: Boolean,
    precio: Int = 0,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .clickable { onSelect() }
            .border(
                width = if (estaSeleccionado) 4.dp else 1.dp,
                color = if (estaSeleccionado) Color(0xFFFFA500) else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        if (esTienda) {
            // Muestra el precio específico de cada bebida
            Text(
                text = "🪙 $precio",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500)
            )
        } else {
            Text(text = "Poseído: 0", fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ActionPanel(esTienda: Boolean, cantidad: Int, onCantidadChange: (Int) -> Unit, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selector de cantidad: - [X] +
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (cantidad > 1) onCantidadChange(cantidad - 1) }) {
                Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Surface(
                modifier = Modifier.width(60.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            ) {
                Text(text = "$cantidad", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }
            IconButton(onClick = { onCantidadChange(cantidad + 1) }) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (esTienda) Color(0xFFFFA500) else Color(0xFF2196F3))
        ) {
            Text(text = if (esTienda) "COMPRAR" else "ALIMENTAR")
        }
    }
}