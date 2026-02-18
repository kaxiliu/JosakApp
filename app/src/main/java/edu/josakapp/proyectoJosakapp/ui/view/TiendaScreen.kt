package edu.josakapp.proyectoJosakapp.ui.view

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.data.model.TiendaItem
import edu.josakapp.proyectoJosakapp.data.model.User

@Composable
fun TiendaScreen(user: User) {
    // Simulamos una lista de items (luego vendrán de tu base de datos)
    val items = listOf(
        TiendaItem(1, "Gorro Pro", 50, R.drawable.ic_menu_agenda, "Gorros"),
        TiendaItem(2, "Gafas Sol", 30, R.drawable.ic_menu_camera, "Gafas"),
        TiendaItem(3, "Bufanda Azul", 20, R.drawable.ic_menu_compass, "Ropa"),
        TiendaItem(4, "Skin Dorada", 500, R.drawable.ic_menu_gallery, "Cuerpo")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- CABECERA: SALDO DE PUNTOS ---
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
                    Text("Tus Puntos", color = Color.White, fontSize = 14.sp)
                    // Aquí usamos los puntos reales del objeto user
                    Text("${user.puntos}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Artículos disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        // --- GRID DE PRODUCTOS ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Dos columnas
            contentPadding = PaddingValues(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                ItemTiendaCard(item, user.puntos)
            }
        }
    }
}

@Composable
fun ItemTiendaCard(item: TiendaItem, puntosUsuario: Int) {
    val puedeComprar = puntosUsuario >= item.precio

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del objeto
            Icon(
                painter = painterResource(id = item.imagen),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = if (puedeComprar) Color.Unspecified else Color.Gray
            )

            Text(item.nombre, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))

            // Etiqueta de precio
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.precio} pts",
                    color = if (puedeComprar) Color(0xFF4CAF50) else Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { /* Lógica de compra */ },
                enabled = puedeComprar,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (puedeComprar) Color(0xFF03A9F4) else Color.LightGray
                )
            ) {
                Text(if (puedeComprar) "Canjear" else "Insuficiente", fontSize = 12.sp)
            }
        }
    }
}