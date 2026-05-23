package edu.josakapp.proyectoJosakapp.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.di.AppModule.pinguinoRepository
import edu.josakapp.proyectoJosakapp.ui.components.ActionPanel
import edu.josakapp.proyectoJosakapp.ui.components.BaseBottomSheetContent
import edu.josakapp.proyectoJosakapp.ui.components.DraggablePenguin
import edu.josakapp.proyectoJosakapp.ui.components.MochilaBebidasGrid
import edu.josakapp.proyectoJosakapp.ui.components.MochilaRopaGrid
import edu.josakapp.proyectoJosakapp.ui.components.TiendaBebidasGrid
import edu.josakapp.proyectoJosakapp.ui.components.TiendaRopaGrid
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
    // Lógica de mensaje: Si faltan hábitos, pregunta por ellos
    val mensaje = remember(listaHabitos, registros) {
        val habitosPendientes = listaHabitos.find { habit ->
            registros.none { it.id_habito == habit.id_habito && it.fecha == hoy }
        }
        habitosPendientes?.let { "¿Has hecho ${it.nombre} hoy?" }
            ?: "¡Todo completado! Eres genial."
    }

    val nivelSed by pinguinoViewModel.nivelSed.collectAsState()
    val mochila by pinguinoViewModel.mochila.collectAsState()
    val userState by userViewModel.user.collectAsState()


    //  ESTADOS PARA LA DAR DE AGUA
    var showSheet by remember { mutableStateOf(false) } // Controla si el BottomSheet es visible
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Mochila, 1: Tienda
    var itemSeleccionado by remember { mutableStateOf<Int?>(null) } // Guarda el índice del item clicado
    var cantidad by remember { mutableIntStateOf(1) } // Cantidad para comprar o alimentar
    val titulosTabs = listOf("Mochila", "Tienda")
    val listaBebidas =
        listOf(R.drawable.bebida1, R.drawable.bebida2, R.drawable.bebida3, R.drawable.bebida4)
    // Define los precios correspondientes a cada bebida
    val preciosBebidas = mapOf(
        R.drawable.bebida1 to 5, R.drawable.bebida2 to 20,
        R.drawable.bebida3 to 15, R.drawable.bebida4 to 10
    )

    // ESTADOS PARA EL MENÚ DE ROPA
    var showRopaSheet by remember { mutableStateOf(false) }// Controla si el BottomSheet de ropa es visible
    var itemRopaSeleccionado by remember { mutableStateOf<Int?>(null) }// Guarda el índice del item de ropa clicado
    var selectedRopaTabIndex by remember { mutableIntStateOf(0) }// 0: Mochila, 1: Tienda
    val titulosRopaTabs = listOf("Mochila", "Tienda")
    val mochilaRopa by pinguinoViewModel.mochilaRopa.collectAsState()
    val ropaEquipadaRes by pinguinoViewModel.ropaEquipadaRes.collectAsState()


    val userId = userState?.id_usuario //Utilizamos el ID del usuario para cargar la ropa comprada y el estado del pingüino. Si userState es null

    //LaunchedEffect se ejecutará cada vez que userId cambie,
    // lo que es útil para cargar los datos del pingüino cuando el usuario inicia sesión o cambia.
    LaunchedEffect(userId) {
        if (userId != null && userId != 0) {
            // Cargar la ropa comprada
            pinguinoViewModel.cargarRopaComprada(userId)

            // Cargar la mochila persistente de bebidas compradas
            pinguinoViewModel.cargarMochila(userId)
            // Cargar la ropa equipada persistente
            pinguinoViewModel.cargarEquipada(userId)

            // Cargar el estado del pingüino
            withContext(Dispatchers.IO) {
                val pinguinoData = pinguinoRepository.getPinguino(userId)

                withContext(Dispatchers.Main) {
                    pinguinoData?.let {
                        pinguinoViewModel.inicializarEstadoPinguino(
                            nivelGuardado = it.nivelSed,
                            ultimaFecha = it.ultimaVezSed
                        )
                    }
                }
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
            Button(
                onClick = { showSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Dar de beber")
            }
            Button(
                onClick = { showRopaSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
            ) {
                Text("Cambiar ropa")
            }
        }

        DraggablePenguin(
            nivelSed = nivelSed, // Muestra la barra azul
            message = null,
            accesorioResId = ropaEquipadaRes
        )

        // Mostramos el BottomSheet para dar de beber solo si showSheet es true
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                BaseBottomSheetContent(
                    selectedTabIndex = selectedTabIndex,
                    titulosTabs = titulosTabs,
                    onTabSelected = {
                        selectedTabIndex = it; itemSeleccionado = null; cantidad = 1
                    },
                    onCloseClick = { showSheet = false }
                ) {
                    if (selectedTabIndex == 0) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (mochila.isEmpty()) {
                                Text(
                                    text = "Mochila vacía",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                MochilaBebidasGrid(
                                    mochila, itemSeleccionado,
                                    { itemSeleccionado = it })
                            }
                        }
                    } else {
                        TiendaBebidasGrid(
                            listaBebidas, preciosBebidas,
                            mochila, itemSeleccionado, { itemSeleccionado = it })
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
                            val currentUserId = userState?.id_usuario ?: 0

                            if (selectedTabIndex == 1) {
                                //Calculamos el costo total de la compra
                                val precioUnitario = preciosBebidas[resId] ?: 0
                                val costoTotal = precioUnitario * cantidad
                                val monedasActuales = userState?.monedas ?: 0

                                    if (monedasActuales >= costoTotal) {
                                    pinguinoViewModel.comprarBebida(resId, cantidad, currentUserId)
                                    userViewModel.updateMonedas(-costoTotal) //Si el usuario tiene suficiente dinero, procedemos con la compra


                                    Toast.makeText(
                                        context,
                                        "¡Compra exitosa!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Reseteamos la selección y cantidad para la próxima compra
                                    cantidad = 1

                                } else {
                                    // Si el usuario no tiene suficiente dinero, mostramos un mensaje de error
                                    Toast.makeText(
                                        context,
                                        "No tienes suficientes monedas.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Verificamos que tengamos suficiente cantidad en la mochila antes de alimentar
                                val cantidadDisponible = mochila[resId] ?: 0
                                if (cantidadDisponible >= cantidad) {
                                    // Si estamos en la mochila, usamos la bebida seleccionada para alimentar al pingüino
                                    pinguinoViewModel.usarBebida(resId, cantidad, currentUserId)
                                    itemSeleccionado = null
                                    showSheet = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "No tienes suficientes bebidas.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
            }
        }

        // Mostramos el BottomSheet para cambiar la ropa solo si showRopaSheet es true
        if (showRopaSheet) {
            ModalBottomSheet(
                onDismissRequest = { showRopaSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                BaseBottomSheetContent(
                    selectedTabIndex = selectedRopaTabIndex,
                    titulosTabs = titulosRopaTabs,
                    onTabSelected = { selectedRopaTabIndex = it; itemRopaSeleccionado = null },
                    onCloseClick = { showRopaSheet = false }
                ) {
                    if (selectedRopaTabIndex == 0) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            if (mochilaRopa.isEmpty()) {
                                Text(
                                    text = "Mochila vacía",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                MochilaRopaGrid(
                                    mochilaRopa,
                                    ropaEquipadaRes,
                                    itemRopaSeleccionado,
                                    { itemRopaSeleccionado = it })
                            }
                        }
                    } else {
                        TiendaRopaGrid(
                            pinguinoViewModel.tiendaDeRopa,
                            mochilaRopa,
                            itemRopaSeleccionado,
                            onItemSelected = { resId ->
                                val accesorioNombre = pinguinoViewModel.tiendaDeRopa.find { acc ->
                                    context.resources.getIdentifier(acc.imagen, "drawable", context.packageName) == resId
                                }?.imagen
                                if (accesorioNombre == null || !mochilaRopa.containsKey(accesorioNombre)) {
                                    itemRopaSeleccionado = resId
                                }
                            }
                        )
                    }
                }

                // Comprar ropa: Solo mostramos el botón de comprar si estamos en la tienda y hay un item seleccionado
                if (itemRopaSeleccionado != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val esTiendaRopa = selectedRopaTabIndex == 1

                    Button(
                        onClick = {
                            val currentUserId = userState?.id_usuario ?: 0
                            if (esTiendaRopa) {
                                // Si estamos en la tienda, intentamos comprar el accesorio seleccionado
                                val clickedAccesorio = pinguinoViewModel.tiendaDeRopa.find { accesorio ->
                                    val targetResId = context.resources.getIdentifier(
                                        accesorio.imagen,
                                        "drawable",
                                        context.packageName
                                    )
                                    // REPARADO: Se asegura una validación no nula y exacta con el recurso actual
                                    targetResId != 0 && targetResId == itemRopaSeleccionado
                                }

                                if (clickedAccesorio != null) {
                                    val precio = clickedAccesorio.precio.toInt()
                                    val monedasActuales = userState?.monedas ?: 0

                                    if (monedasActuales >= precio) {
                                        // REPARADO: Primero guardamos el estado en el ViewModel antes de restar monedas para mantener el flujo de recomposición sincronizado
                                        pinguinoViewModel.comprarRopa(
                                            resId = itemRopaSeleccionado!!,
                                            accesorio = clickedAccesorio,
                                            userId = currentUserId
                                        )
                                        userViewModel.updateMonedas(-precio)
                                        Toast.makeText(context, "¡Compra exitosa!", Toast.LENGTH_SHORT).show()
                                        itemRopaSeleccionado = null
                                    } else {
                                        Toast.makeText(context, "No tienes suficientes monedas.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Error al identificar el accesorio.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val currentUserId = userState?.id_usuario ?: 0

                                    itemRopaSeleccionado?.let { resId ->
                                    pinguinoViewModel.equiparRopa(resId, currentUserId)

                                    Toast.makeText(context, "¡Ropa cambiada!", Toast.LENGTH_SHORT).show()
                                }
                                // Si estamos en la mochila, equipamos la ropa seleccionada
                                itemRopaSeleccionado = null
                                showRopaSheet = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (esTiendaRopa) Color(0xFFFFA500) else Color(0xFF2196F3)
                        )
                    ) {
                        Text(text = if (esTiendaRopa) "COMPRAR" else "EQUIPAR")
                    }
                }
            }
        }
    }
}