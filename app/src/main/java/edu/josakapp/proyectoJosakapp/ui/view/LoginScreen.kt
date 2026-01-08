package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.ui.theme.verdeNeon


/**Pantalla encargada de el login del usuario*/

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(name: String,
               onNameChange: (String) -> Unit,
               onGoSecondScreen: () -> Unit,
               onGoRegisterScreen: () -> Unit,
               onGoForgotPasswordScreen: () -> Unit){
    var name by remember {mutableStateOf("")}
    var pass by remember {mutableStateOf("")}
    Card (
        modifier = Modifier.fillMaxSize()
    ){
        /**Agregamos Box, para que supersponga el fondo*/
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            encabezadoHome()
            /**Agregamos la imagen*/
            Image(
                painter = painterResource(id = R.drawable.fondo_claro),
                contentDescription = "Fondo",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp)
                 //   .matchParentSize()
                //    .background(Color.Black.copy(alpha = 0.6f))
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp)),
                 //   .background(Color.White.copy(alpha = 0.7f))
            )
            cuerpoHome(onGoSecondScreen,
                onGoRegisterScreen, onGoForgotPasswordScreen)

        }
    }
}
/**Aqui nos encargaremos del encabezado y cuerpo */
@Composable
fun encabezadoHome() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Text(text = stringResource(R.string.nombre_empresa),
            color = verdeNeon,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
        )
        Text(text = stringResource(R.string.usuario),
            color = verdeNeon,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(15.dp)
        )
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "icono",
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
    }
}
@Composable
fun cuerpoHome(onGoSecondScreen: () -> Unit,
               onGoRegisterScreen: () -> Unit,
               onGoForgotPasswordScreen: () -> Unit   ){
    var name by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        /**Agregamos icono
        Icon(
            painter=painterResource(R.drawable.outline_chess_queen_24),
            contentDescription = "icono",
            modifier = Modifier
                .height(100.dp)
                .width(100.dp),
            tint = Color.White
        ) */
        Text(text = stringResource(R.string.acceso),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = name,
            onValueChange = {name=it},
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "name")
            },
            placeholder = {
                Text(text = stringResource(R.string.introduce_nombre))
            },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .width(280.dp)
                .border(1.dp, Color.White, RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = pass,
            onValueChange = { pass = it },
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_password_24), contentDescription = "pass")
            },
            placeholder = {
                Text(text = stringResource(R.string.introduce_contrasena))
            },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .width(280.dp) // 👈 mismo ancho que el de arriba
                .border(1.dp, Color.White, RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true,
            label = {   Text(text = stringResource(R.string.introduce_contrasena)) },
            visualTransformation = PasswordVisualTransformation()
        )


        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.registrarse),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable{onGoRegisterScreen()}
            )
            Text(text = stringResource(R.string.contrasena_olvidada),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable{onGoForgotPasswordScreen()}
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onGoSecondScreen,
            modifier = Modifier.width(200.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.entrar),
                fontSize = 18.sp)
        }

        /***Despues del boton se debe de añadir un acceso rapido */
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            socialLoginIcon(R.drawable.ic_google) {/*Accion de entrar con google*/}
            socialLoginIcon(R.drawable.ic_facebook) {/*Accion de entrar con google*/}
          //  socialLoginIcon(R.drawable.ic_twitter) {/*Accion de entrar con google*/}
            socialLoginIcon(R.drawable.ic_appel) {/*Accion de entrar con google*/}
        }
    }
}

/**Implementamos los iconos pero se podria implementar en forma de imagen*/
@Composable
fun socialLoginIcon(iconRes: Int, onClick: () -> Unit){
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = Modifier
            .size(50.dp)
            .clickable(onClick = onClick)
    )
}

