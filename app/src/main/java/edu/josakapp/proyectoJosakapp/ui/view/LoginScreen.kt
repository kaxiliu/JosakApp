package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.ui.components.cuerpoHome

/**Pantalla encargada de el login del usuario*/
/**Ver parametros de name y pass*/
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
    name: String,
    onNameChange: (String) -> Unit,
    onGoSecondScreen: () -> Unit,
    onGoRegisterScreen: () -> Unit,
    onGoForgotPasswordScreen: () -> Unit
) {
    var pass by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.fondo_claro),
                contentDescription = "Fondo",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
                    .background(
                        Color.White.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .border(1.dp, Color.White.copy(0.3f), RoundedCornerShape(28.dp))
            ) {
                cuerpoHome(
                    name = name,
                    pass = pass,
                    onNameChange = onNameChange,
                    onPassChange = { pass = it },
                    onGoSecondScreen = onGoSecondScreen,
                    onGoRegisterScreen = onGoRegisterScreen,
                    onGoForgotPasswordScreen = onGoForgotPasswordScreen
                )
            }
        }
    }
}




/* TextButton(onClick = { navController.navigate("forgot_password") }) {
    Text("¿Olvidaste tu contraseña?")
}
*/
