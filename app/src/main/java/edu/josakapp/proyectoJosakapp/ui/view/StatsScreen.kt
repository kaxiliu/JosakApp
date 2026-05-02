package edu.josakapp.proyectoJosakapp.ui.view


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.components.CalendarCard
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController,
                registros: List<HabitoRegistro>,
                userViewModel: UserViewModel,
                habitosViewModel: HabitosViewModel
) {
    val user by userViewModel.user.collectAsState()
   //val totalDiasLogrados = registros.map { it.fecha }.distinct().size
    val rachaActual by habitosViewModel.rachaActual.collectAsState()
    val totalDiasActivos by habitosViewModel.totalDiasActivos.collectAsState()
    val registros by habitosViewModel.todosLosRegistros.collectAsState()
    val xpActual = user?.xp_total ?: 0 //    XP total del usuario

    LaunchedEffect(Unit) {
        user?.id_usuario?.let { userViewModel.refreshUser(it) }
    }


    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "Días de Constancia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {


            //Experiencia total del usuario
            item {
                // Estado para controlar la visibilidad del diálogo de nivel
                var showLevelDialog by remember { mutableStateOf(false) }

                // Obtener nivel actual y calcular progreso basado en las funciones de la clase User
                val currentLevel = user?.nivel ?: 1
                val progress = User.calculateLevelProgress(xpActual, currentLevel)
                val requiredXp = User.getRequiredXpForNextLevel(currentLevel)
                val xpRemaining = requiredXp - (xpActual % requiredXp)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF8E7),
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐ $xpActual XP",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        // Botón circular que muestra el nivel actual del usuario
                        Surface(
                            onClick = { showLevelDialog = true }, // Abrir diálogo al hacer clic
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = Color.LightGray,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "$currentLevel",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Diálogo detallado del nivel y progreso
                if (showLevelDialog) {
                    LevelDetailDialog(
                        level = currentLevel,
                        progress = progress,
                        xpRemaining = xpRemaining.toLong(),
                        onDismiss = { showLevelDialog = false }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Mostrar el número de días logrados
                            Text(
                                text = "$rachaActual",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF90EE90)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "días seguidos",
                                fontSize = 14.sp,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 1.dp)
                            )
                        }

                        // Mostrar el esfuerzo total del usuario
                        Text(
                            text = "Esfuerzo total: $totalDiasActivos días",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
            item {
                CalendarCard(
                    registros = registros,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                val habitos by habitosViewModel.habitos.collectAsState()

                MisionesPrincipianteCard(
                    user = user,
                    habitos = habitos,
                    onReclamarXP = { tipo ->
                        // Pasamos un identificador para cada misión
                        user?.id_usuario?.let { userViewModel.completarMision(it, tipo, 200) }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "OTROS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Canjear monedas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🪙 Canjear monedas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            IconButton(onClick = { navController.navigate("money") }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Ir a monedas",
                                    tint = Color.Gray
                                )
                            }
                        }

                        // Ir a la tienda
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🛒 Ir a la tienda",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            IconButton(onClick = { navController.navigate(NavScreens.NavTiendaScreen.ruta) }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Ir a tienda",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MisionesPrincipianteCard(
    user: User?,
    habitos: List<Habito>,
    onReclamarXP: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    // Condición 1: Tener 3 o más hábitos
    val misionHabitosLista = habitos.size >= 3

    // Condición 2: Perfil completo (Foto, nombre y teléfono no pueden estar vacíos/0)
    val misionPerfilLista = user?.let {
        it.nombre_usuario != "Nuevo usuario" &&
                it.fotoPerfil.isNotEmpty() &&
                it.telefono != 0
    } ?: false

    //Usamos LaunchedEffect para reclamar XP automáticamente cuando se cumpla cada misión,
    // así el usuario no tiene que hacer click en nada
    LaunchedEffect(misionHabitosLista) {
        if (misionHabitosLista) {
            onReclamarXP("habitos")
        }
    }

    LaunchedEffect(misionPerfilLista) {
        if (misionPerfilLista) {
            onReclamarXP("perfil")
        }
    }

    // Si todo está terminado, el bloque se oculta automáticamente
    if (misionHabitosLista && misionPerfilLista) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(), // Para que se cierre suavemente
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título con opción de colapsar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MISIONES DE INICIO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                IconButton(onClick = { isExpanded = !isExpanded }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                // Misión 1: Hábitos
                MisionRow(
                    texto = "Añadir 3 hábitos (${habitos.size}/3)",
                    estaCompletada = misionHabitosLista,
                    recompensa = 200,
                    onReclamar = { onReclamarXP("habitos") }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F5F5))

                // Misión 2: Perfil
                MisionRow(
                    texto = "Completar tu perfil (Foto y Teléfono)",
                    estaCompletada = misionPerfilLista,
                    recompensa = 200,
                    onReclamar = { onReclamarXP("perfil") }
                )
            }
        }
    }
}

@Composable
fun MisionRow(texto: String, estaCompletada: Boolean, recompensa: Int, onReclamar: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (estaCompletada) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (estaCompletada) Color(0xFF90EE90) else Color.LightGray
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = texto,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            // Si está completada, tachamos el texto
            textDecoration = if (estaCompletada) TextDecoration.LineThrough else TextDecoration.None,
            color = if (estaCompletada) Color.Gray else Color.Black
        )
        Text(
            text = "+$recompensa XP",
            color = Color(0xFFFFC107),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

/**
 * Diálogo que muestra el nivel del usuario, el progreso actual y cuánto XP falta para el siguiente nivel.
 */
@Composable
fun LevelDetailDialog(
    level: Int,
    progress: Float,
    xpRemaining: Long,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón de cierre (X) en la esquina superior derecha
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }

                // Título con el nivel actual
                Text(
                    text = "NIVEL $level",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Barra de progreso visual
                // Asegúrate de usar los modificadores de forma directa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)) // Importación necesaria
                        .background(Color(0xFFF5F5F5)) // Importación necesaria
                ) {
                    // Parte llena de la barra de progreso
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress) // Proporción dinámica basada en el progreso
                            .fillMaxHeight()
                            .background(Color(0xFF90EE90)) // Verde claro
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Texto informativo sobre el XP restante
                Text(
                    text = "Te faltan $xpRemaining XP para el nivel ${level + 1}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}