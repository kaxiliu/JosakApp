package edu.josakapp.proyectoJosakapp.ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyScreen(navController: NavController, userViewModel: UserViewModel) {

    val userState by userViewModel.user.collectAsState()
    val monedasActuales = userState?.monedas ?: 0

    // Estado para controlar la visibilidad del regalo y el diálogo de recompensa
    val context = LocalContext.current
    var mostrarRegalo by remember { mutableStateOf(userViewModel.obtenerEstadoRegalo(context)) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var yaAbierto by remember { mutableStateOf(false) }// Controla si ya se mostró el premio
    var monedasGanadas by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            if (mostrarRegalo) {
                FloatingActionButton(
                    onClick = {
                        yaAbierto = false
                        mostrarDialogo = true
                    },
                    containerColor = Color(0xFFFFD700),
                    shape = CircleShape
                ) {
                    Text(text = "🎁", fontSize = 24.sp)
                }
            }
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tus Monedas",
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "$monedasActuales",
                    fontSize = 48.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFA500)
                )

                Spacer(modifier = Modifier.height(20.dp))
                CheckInCard(userViewModel)
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        navController.navigate(NavScreens.NavTiendaScreen.ruta) {
                            launchSingleTop = true
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7BB7F5),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.width(160.dp).height(50.dp)
                ) {
                    Text(
                        text = "Tienda",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Diálogo para el regalo misterioso
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { if (yaAbierto) mostrarDialogo = false },
            confirmButton = {},
            dismissButton = {}, // No usamos los botones por defecto
            // Dejamos el title vacío para que no genere espacio extra arriba
            title = null,
            text = {
                // Usamos una Column para meterlo todo en el área de contenido
                Column(
                    modifier = Modifier.fillMaxWidth().animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //  Botón X en la parte más alta posible
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { mostrarDialogo = false },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Gray
                            )
                        }
                    }

                    //  Título (ahora debajo de la X)
                    Text(
                        text = if (yaAbierto) "¡Felicidades!" else "Regalo Misterioso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    //  Contenido Principal (Misterio o Recompensa)
                    if (!yaAbierto) {
                        // Cuando aún no se ha abierto, mostramos el misterio
                        Text(text = "¿Qué habrá dentro?", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                userState?.id_usuario?.let { uid ->
                                    //  Solo aquí se ejecuta el reclamo real
                                    monedasGanadas =
                                        userViewModel.reclamarRegaloAleatorio(context, uid)
                                    yaAbierto = true
                                    mostrarRegalo = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ABRIR REGALO", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Cuando ya se ha abierto, mostramos la recompensa real
                        Text(text = "Has recibido", fontSize = 16.sp)
                        Text(
                            text = " $monedasGanadas 🪙",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFA500)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "¡Vuelve en 24 horas por más!",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}



// Componente para cada día de recompensa
@Composable
fun CheckInCard(userViewModel: UserViewModel) {
    val context = LocalContext.current
    val userState by userViewModel.user.collectAsState()

    // Solo para simplificar, obtenemos el estado del check-in directamente aquí.
    var status by remember { mutableStateOf(userViewModel.obtenerEstadoCheckIn(context)) }
    val nextDayToClaim = status.first
    val yaReclamadoHoy = status.second

    //Validamos que no se esté procesando un check-in para evitar múltiples clics rápidos
    var isProcessing by remember { mutableStateOf(false) }

    val recompensas = listOf(10, 20, 30, 40, 60, 80, 100)

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Titulo del card
            Text(
                text = "Recompensa Diaria",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recompensas.forEachIndexed { index, monto ->
                    val dayIndex = index + 1
                    val isDone = dayIndex < nextDayToClaim || (dayIndex == nextDayToClaim && yaReclamadoHoy)
                    val isCurrent = dayIndex == nextDayToClaim && !yaReclamadoHoy
                    val isFuture = dayIndex > nextDayToClaim

                    // Cada día con su propio estado visual y lógica de clic
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "D$dayIndex",
                            fontSize = 10.sp,
                            color = if (isCurrent) Color.Black else Color.Gray,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Cuando se hace clic en un día, solo si es el día actual
                        // y no se ha reclamado, se ejecuta la función de check-in
                        Surface(
                            onClick = {
                                if (isCurrent && !isProcessing) {
                                    isProcessing = true // Bloqueo físico inmediato
                                    userState?.id_usuario?.let { uid ->
                                        // Ejecutamos la lógica
                                        userViewModel.ejecutarCheckInManual(context, dayIndex, uid, monto)

                                        // Usamos un pequeño retraso para asegurar que SharedPreferences se guardó
                                        // O mejor aún, deja que el Compose observe el cambio de userState
                                        status = Pair(dayIndex, true) // Forzamos el estado visual localmente

                                        // Un pequeño delay antes de permitir otro clic por seguridad
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                            isProcessing = false
                                        }, 500)
                                    }
                                } else if (isFuture || yaReclamadoHoy) {
                                    // Mensaje de advertencia
                                    Toast.makeText(context, "Ya lo has recogido hoy, vuelve mañana", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = true,
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isDone -> Color(0xFF90EE90) // Verde claro para días reclamados
                                isCurrent -> Color(0xFFFFC107).copy(alpha = 0.2f) // Amarillo claro para el día actual
                                else -> Color(0xFFF5F5F5) //Gris claro para días futuros
                            },
                            modifier = Modifier.aspectRatio(1f).fillMaxWidth()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                } else {
                                    Text(
                                        text = "$monto",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color(0xFFFFC107) else Color.Gray
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
