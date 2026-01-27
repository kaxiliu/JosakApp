package edu.josakapp.proyectoJosakapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.R

/**Se decidio crear este archivo para que se vea más ordenado*/
/**Aqui tenemos todo lo que sería el cuerpo de nuestro LoginScreen*/
@Composable
fun cuerpoHome(onGoSecondScreen: () -> Unit,
               onGoRegisterScreen: () -> Unit,
               onGoForgotPasswordScreen: () -> Unit   ){
    var name by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = name,
            onValueChange = {name=it},
            placeholder = {
                Text(text = stringResource(R.string.introduce_nombre))
            },
            modifier = Modifier
                .width(280.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = pass,
            onValueChange = { pass = it },
            placeholder = {
                Text(text = stringResource(R.string.introduce_contrasena))
            },
            modifier = Modifier
                .width(280.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.LightGray
            ),
            singleLine = true,
            label = {   Text(text = stringResource(R.string.introduce_contrasena)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onGoSecondScreen,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7BB7F5),
                contentColor = Color.White
            ),
            modifier = Modifier.width(130.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.entrar),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.registrarse),
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable{onGoRegisterScreen(/**Le tendra que llegar a registrar el correo*/)}
            )
            /**Separador entre registrarse y olvide la contraseña*/
            Text(
                text = "|",
                color = Color.Gray,
                fontSize = 29.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.contrasena_olvidada),
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable{onGoForgotPasswordScreen(/**Ir a pantalla de contraseña olgidad */)}
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.width(280.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.LightGray))
            Text(
                text = stringResource(R.string.txt_acceso_rapido),
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.LightGray))
        }
        Spacer(modifier = Modifier.height(10.dp))
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
