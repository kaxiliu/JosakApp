package edu.josakapp.proyectoJosakapp.ui.view


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.ui.components.CalendarCard
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController,
                registros: List<HabitoRegistro>,
                userViewModel: UserViewModel
) {
    val user by userViewModel.user.collectAsState()
    val totalDiasLogrados = registros.map { it.fecha }.distinct().size
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            //Experiencia total del usuario
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF8E7),
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ $xpActual XP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107) ,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

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
                    text = "$totalDiasLogrados",
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
            Spacer(modifier = Modifier.height(20.dp))
            CalendarCard(registros = registros,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp))
                }
            }
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