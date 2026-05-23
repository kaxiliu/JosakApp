package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.Duration
import edu.josakapp.proyectoJosakapp.data.store.StoreViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel
import edu.josakapp.proyectoJosakapp.data.store.StoreItem
@Composable
fun TiendaScreen(userViewModel: UserViewModel, storeViewModel: StoreViewModel = viewModel()) {
    val items by storeViewModel.items.collectAsState()
    val dailyItems by storeViewModel.dailyItems.collectAsState()
    val userState by userViewModel.user.collectAsState()
    val monedas = userState?.monedas ?: 0

    val snackbarHostState = androidx.compose.material3.SnackbarHostState()
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header: saldo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF03A9F4)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tus Monedas", color = Color.White, fontSize = 14.sp)
                        Text("$monedas", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Countdown to 08:00 GMT+2 (llama a refresh al rotar)
            val rotateTrigger = remember { mutableStateOf(0) }
            CountdownTo8GMT2(storeViewModel = storeViewModel, onRotate = {
                rotateTrigger.value = rotateTrigger.value + 1
            })

            Spacer(modifier = Modifier.height(12.dp))

            // Ofertas del día (únicamente)
            Text("Ofertas del día", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            val ownedMap = items.associate { it.item.id to it.isOwned }
            val availableDaily = dailyItems.filter { ownedMap[it.id] != true }

            // Animación de refresco cuando rota
            val scale = remember { Animatable(1f) }
            LaunchedEffect(rotateTrigger.value) {
                if (rotateTrigger.value > 0) {
                    scale.animateTo(1.06f, animationSpec = tween(250))
                    scale.animateTo(1f, animationSpec = tween(300))
                }
            }

            if (availableDaily.isEmpty()) {
                Text("No hay ofertas disponibles hoy", fontWeight = FontWeight.Medium)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                ) {
                    availableDaily.forEach { di ->
                        item {
                            ItemTiendaCard(di, false, monedas, onBuy = { buyItem ->
                                val canBuy = monedas >= buyItem.price
                                if (!canBuy) {
                                    scope.launch { snackbarHostState.showSnackbar("No tienes suficientes monedas") }
                                    return@ItemTiendaCard
                                }
                                if (buyItem.id == "money_pack") userViewModel.updateMonedas(200)
                                else {
                                    userViewModel.updateMonedas(-buyItem.price)
                                    storeViewModel.markOwned(buyItem.id)
                                }
                                scope.launch { snackbarHostState.showSnackbar("Compra realizada") }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemTiendaCard(item: StoreItem, isOwned: Boolean, monedasUsuario: Int, onBuy: (StoreItem) -> Unit) {
    val puedeComprar = monedasUsuario >= item.price && !isOwned
    val accent = rarityColor(item.rarity)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, accent),
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen
            if (item.imageResId != 0) {
                val iconBg = categoryColor(item.category).copy(alpha = if (puedeComprar) 0.25f else 0.12f)
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(52.dp),
                        tint = if (puedeComprar) Color.Unspecified else Color.Gray
                    )
                }
            }

            Text(item.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp), color = accent)

            Text(item.description, style = MaterialTheme.typography.bodySmall)

            Text(
                text = item.rarity.replaceFirstChar { it.uppercase() },
                color = accent,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.price} pts",
                    color = if (puedeComprar) Color(0xFF4CAF50) else Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { onBuy(item) },
                enabled = puedeComprar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (puedeComprar) Color(0xFF03A9F4) else Color.LightGray
                )
            ) {
                Text(if (isOwned) "Comprado" else if (puedeComprar) "Comprar" else "Insuficiente", fontSize = 12.sp)
            }
        }
    }
}

private fun rarityColor(rarity: String): Color {
    return when (rarity.lowercase()) {
        "common" -> Color(0xFF4CAF50)
        "rare" -> Color(0xFF2196F3)
        "epic" -> Color(0xFF9C27B0)
        "legendary" -> Color(0xFFFF9800)
        else -> Color(0xFF607D8B)
    }
}

private fun categoryColor(category: String): Color {
    return when (category.lowercase()) {
        "ropa" -> Color(0xFF03A9F4)
        "accesorio" -> Color(0xFF8BC34A)
        "cuerpo" -> Color(0xFFFF9800)
        "efecto" -> Color(0xFF9C27B0)
        "consumible" -> Color(0xFF009688)
        else -> Color(0xFF607D8B)
    }
}

@Composable
fun CountdownTo8GMT2(storeViewModel: StoreViewModel, onRotate: () -> Unit) {
    val remainingMillis = remember { mutableStateOf(computeMillisUntilNext8GMT2()) }
    LaunchedEffect(Unit) {
        var prev = remainingMillis.value
        while (true) {
            val current = computeMillisUntilNext8GMT2()
            remainingMillis.value = current
            // Si antes quedaban pocos ms y ahora ha saltado a ~24h -> hubo rotación
            if (prev <= 2000L && current > prev) {
                try { storeViewModel.refresh() } catch (_: Exception) {}
                try { onRotate() } catch (_: Exception) {}
            }
            prev = current
            delay(1000L)
        }
    }

    val dur = Duration.ofMillis(remainingMillis.value.coerceAtLeast(0))
    val hours = dur.toHours()
    val minutes = dur.minusHours(hours).toMinutes()
    val seconds = dur.minusHours(hours).minusMinutes(minutes).seconds

    Text("Siguiente rotación: %02d:%02d:%02d".format(hours, minutes, seconds), fontWeight = FontWeight.Medium)
}

private fun computeMillisUntilNext8GMT2(): Long {
    val zone = ZoneOffset.ofHours(2)
    val now = ZonedDateTime.now(zone)
    var next = now.withHour(8).withMinute(0).withSecond(0).withNano(0)
    if (!next.isAfter(now)) {
        next = next.plusDays(1)
    }
    return Duration.between(now, next).toMillis()
}
