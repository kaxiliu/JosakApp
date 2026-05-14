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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
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
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.ui.components.DraggablePenguin
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.PinguinoViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinguinoScreen(
    userViewModel: UserViewModel,
    habitosViewModel: HabitosViewModel,
    pinguinoViewModel: PinguinoViewModel,
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

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

    val nivelSed by pinguinoViewModel.nivelSed.collectAsState()


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

    val mochila by pinguinoViewModel.mochila.collectAsState()
    val userState by userViewModel.user.collectAsState()

    // Al cargar la pantalla, recuperamos
    // el estado del pingüino desde la base de datos
    LaunchedEffect(userState) {
        userState?.let { user ->
            val db = AppDatabase.getInstance(context)
            val pinguinoData = withContext(Dispatchers.IO) {
                db.usersDAO().getPinguinoByUserId(user.id_usuario)
            }

            pinguinoData?.let {
                //  Inicializamos el estado del pingüino con los datos recuperados de la base de datos
                pinguinoViewModel.inicializarEstadoPinguino(
                    nivelGuardado = it.nivelSed,
                    ultimaFecha = it.ultimaVezSed
                )
            }
        }
    }

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
                                    cantidad = 1 // Resetear cantidad al cambiar tab
                                },
                                text = { Text(title) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CONTENIDO
                    Box(modifier = Modifier.weight(1f)) {
                        if (selectedTabIndex == 0) {
                            // --- MOCHILA DINÁMICA ---
                            if (mochila.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = "Mochila vacía", color = Color.Gray)
                                }
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Obtenemos solo los IDs de los items que realmente tenemos en la mochila
                                    val itemsEnMochila = mochila.keys.toList().chunked(3)
                                    items(itemsEnMochila) { fila ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            fila.forEach { resId ->
                                                ItemBebidaUI(
                                                    resId = resId,
                                                    esTienda = false,
                                                    estaSeleccionado = itemSeleccionado == resId,
                                                    cantidadPoseida = mochila[resId] ?: 0,
                                                    onSelect = { itemSeleccionado = resId }
                                                )
                                            }
                                            repeat(3 - fila.size) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
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
                                            // Recuperamos el precio del mapa para mostrarlo
                                            val precio = preciosBebidas[resId] ?: 0

                                            ItemBebidaUI(
                                                resId = resId,
                                                esTienda = true,
                                                estaSeleccionado = itemSeleccionado == resId,
                                                precio = precio,
                                                cantidadPoseida = mochila[resId] ?: 0,
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
                            onConfirm = {
                                val resId = itemSeleccionado!!
                                val currentUserId = userState?.id_usuario?: 0

                                if (selectedTabIndex == 1) {
                                    //Calculamos el costo total de la compra
                                    val precioUnitario = preciosBebidas[resId] ?: 0
                                    val costoTotal = precioUnitario * cantidad
                                    val monedasActuales = userState?.monedas ?: 0

                                    if (monedasActuales >= costoTotal) {
                                        userViewModel.updateMonedas(-costoTotal) //Si el usuario tiene suficiente dinero, procedemos con la compra
                                        pinguinoViewModel.comprarBebida(resId, cantidad)//Agregamos las bebidas compradas a la mochila del pingüino

                                        android.widget.Toast.makeText(
                                            context,
                                            "¡Compra exitosa!",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()

                                        // Reseteamos la selección y cantidad para la próxima compra
                                        itemSeleccionado = null
                                        cantidad = 1

                                    } else {
                                        // Si el usuario no tiene suficiente dinero, mostramos un mensaje de error
                                        android.widget.Toast.makeText(
                                            context,
                                            "No tienes suficientes monedas.",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    // Verificamos que tengamos suficiente cantidad en la mochila antes de alimentar
                                    val cantidadDisponible = mochila[resId] ?: 0
                                    if (cantidadDisponible >= cantidad) {
                                        // Si estamos en la mochila, usamos la bebida seleccionada para alimentar al pingüino
                                        pinguinoViewModel.usarBebida(resId, cantidad,currentUserId)
                                        itemSeleccionado = null
                                        showSheet = false
                                    } else {
                                        android.widget.Toast.makeText(
                                            context,
                                            "No tienes suficientes bebidas.",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
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
    cantidadPoseida: Int = 0,
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
            // Muestra la cantidad que posees en la mochila
            Text(text = "x$cantidadPoseida", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
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