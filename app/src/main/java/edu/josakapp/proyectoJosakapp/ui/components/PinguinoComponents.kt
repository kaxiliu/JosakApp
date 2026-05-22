package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.data.model.Accesorios
import kotlin.collections.forEach

// Componente para cada bebida,
//cuando seleccionamos una bebida en la tienda,
// se resalta con un borde
@Composable
fun RowScope.ItemUI(
    resId: Int,
    esTienda: Boolean,
    estaSeleccionado: Boolean,
    precio: Int = 0,
    cantidadPoseida: Int = 0,
    textoEstado: String? = null,
    deshabilitado: Boolean = false,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            // Si está deshabilitado (ya comprado), no responde al click
            .clickable(enabled = !deshabilitado) { onSelect() }
            .border(
                width = if (estaSeleccionado) 4.dp else 1.dp,
                color = if (estaSeleccionado) Color(0xFFFFA500) else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            //  Si ya está comprado, se muestra con un tono gris opaco
            .background(if(deshabilitado) Color.LightGray.copy(alpha = 0.5f) else Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        if (esTienda) {
            Text(
                text = "🪙 $precio",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500)
            )
        } else {
            if (textoEstado != null) {
                if (textoEstado.isNotEmpty()) {
                    Text(
                        text = textoEstado,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }
            } else {
                Text(
                    text = "x$cantidadPoseida",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ActionPanel(esTienda: Boolean, cantidad: Int, onCantidadChange: (Int) -> Unit, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (esTienda) Color(0xFFFFA500) else Color(0xFF2196F3))
        ) {
            Text(text = if (esTienda) "COMPRAR" else "ALIMENTAR")
        }
    }
}

@Composable
fun MochilaBebidasGrid(
    mochila: Map<Int, Int>,
    itemSeleccionado: Int?,
    onItemSelected: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        val itemsEnMochila = mochila.keys.toList().chunked(3)
        items(itemsEnMochila) { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fila.forEach { resId ->
                    ItemUI(
                        resId = resId,
                        esTienda = false,
                        estaSeleccionado = itemSeleccionado == resId,
                        cantidadPoseida = mochila[resId] ?: 0,
                        onSelect = { onItemSelected(resId) }
                    )
                }
                repeat(3 - fila.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TiendaBebidasGrid(
    listaBebidas: List<Int>,
    preciosBebidas: Map<Int, Int>,
    mochila: Map<Int, Int>,
    itemSeleccionado: Int?,
    onItemSelected: (Int) -> Unit
) {
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
                    val precio = preciosBebidas[resId] ?: 0

                    ItemUI(
                        resId = resId,
                        esTienda = true,
                        estaSeleccionado = itemSeleccionado == resId,
                        precio = precio,
                        cantidadPoseida = mochila[resId] ?: 0,
                        onSelect = { onItemSelected(resId) }
                    )
                }
                repeat(3 - fila.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TiendaRopaGrid(
    tiendaDeRopa: List<Accesorios>,
    mochilaRopa: Map<String, Boolean>,
    itemRopaSeleccionado: Int?,
    onItemSelected: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        val filas = tiendaDeRopa.chunked(3)
        items(filas) { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fila.forEach { accesorio ->
                    val resId = obtenerDrawableId(accesorio.imagen)
                    val yaComprado = mochilaRopa.containsKey(accesorio.imagen)

                    // Pasamos yaComprado al deshabilitado para congelar los clicks por completo
                    ItemUI(
                        resId = resId,
                        esTienda = !yaComprado,
                        estaSeleccionado = itemRopaSeleccionado == resId,
                        precio = accesorio.precio.toInt(),
                        cantidadPoseida = if (yaComprado) 1 else 0,
                        deshabilitado = yaComprado,
                        onSelect = { onItemSelected(resId) }
                    )
                }
                repeat(3 - fila.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun MochilaRopaGrid(
    mochilaRopa: Map<String, Boolean>,
    ropaEquipadaRes: Int?,
    itemRopaSeleccionado: Int?,
    onItemSelected: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        val itemsMochila = mochilaRopa.keys.toList().chunked(3)
        items(itemsMochila) { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fila.forEach { imagenNombre ->
                    val resId = obtenerDrawableId(imagenNombre)
                    ItemUI(
                        resId = resId,
                        esTienda = false,
                        estaSeleccionado = itemRopaSeleccionado == resId,
                        textoEstado = if (ropaEquipadaRes == resId) "Puesto" else " Disponible",
                        onSelect = { onItemSelected(resId) }
                    )
                }
                repeat(3 - fila.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun BaseBottomSheetContent(
    selectedTabIndex: Int,
    titulosTabs: List<String>,
    onTabSelected: (Int) -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.Transparent) {
            titulosTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.weight(1f),
            content = content
        )
    }
}

@Composable
fun obtenerDrawableId(nombreImagen: String): Int {
    val context = LocalContext.current
    return remember(nombreImagen) {
        context.resources.getIdentifier(nombreImagen, "drawable",
            context.packageName)
    }
}