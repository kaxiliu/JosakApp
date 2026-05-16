package edu.josakapp.proyectoJosakapp.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
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
    var menuTareasExpandido by remember { mutableStateOf(false) }

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
    var mochilaRopa by remember { mutableStateOf(mapOf<Int, Boolean>()) }//Datos de la ropa que el pingüino tiene en su mochila


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
                    DropdownMenuItem(text = { Text("Cambiar ropa") }, onClick = {
                        showRopaSheet = true
                        menuTareasExpandido = false
                    })
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
        }

        DraggablePenguin(
            nivelSed = nivelSed, // Muestra la barra azul
            message = null
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
                                    userViewModel.updateMonedas(-costoTotal) //Si el usuario tiene suficiente dinero, procedemos con la compra
                                    pinguinoViewModel.comprarBebida(
                                        resId,
                                        cantidad
                                    )//Agregamos las bebidas compradas a la mochila del pingüino

                                    Toast.makeText(
                                        context,
                                        "¡Compra exitosa!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Reseteamos la selección y cantidad para la próxima compra
                                    itemSeleccionado = null
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
                        val ropaEquipadaRes = null

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
                            { itemRopaSeleccionado = it })
                    }
                }

                // Comprar ropa: Solo mostramos el botón de comprar si estamos en la tienda y hay un item seleccionado
                if (itemRopaSeleccionado != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val esTiendaRopa = selectedRopaTabIndex == 1

                    Button(
                        onClick = {
                            if (esTiendaRopa) {
                                // Si estamos en la tienda, intentamos comprar el accesorio seleccionado
                                val clickedAccesorio = pinguinoViewModel.tiendaDeRopa.find { accesorio ->
                                    val resId = context.resources.getIdentifier(
                                        accesorio.imagen,
                                        "drawable",
                                        context.packageName
                                    )
                                    resId == itemRopaSeleccionado
                                }
                                val precio = clickedAccesorio?.precio?.toInt() ?: 0
                                val monedasActuales = userState?.monedas ?: 0

                                if (monedasActuales >= precio) {
                                    userViewModel.updateMonedas(-precio)
                                    mochilaRopa = mochilaRopa + (itemRopaSeleccionado!! to true)
                                    Toast.makeText(context, "¡Compra exitosa!", Toast.LENGTH_SHORT).show()
                                    itemRopaSeleccionado = null
                                } else {
                                    Toast.makeText(context, "No tienes suficientes monedas.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val currentUserId = userState?.id_usuario ?: 0
                                //FALTA: Aquí deberíamos actualizar el estado del pingüino para equipar la ropa seleccionada, lo que podría implicar una función en el ViewModel que marque esa ropa como equipada y posiblemente afecte la apariencia del pingüino

                                Toast.makeText(context, "¡Ropa cambiada!", Toast.LENGTH_SHORT).show()

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
