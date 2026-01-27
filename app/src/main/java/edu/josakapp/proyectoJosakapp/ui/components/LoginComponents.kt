package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**Archivo que se encargará de los iconos de el loginScreen
 * */
/**Implementamos los iconos pero se podria implementar en forma de imagen*/
@Composable
fun socialLoginIcon(iconRes: Int, onClick: () -> Unit){
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        tint = Color.Unspecified
    )
}

