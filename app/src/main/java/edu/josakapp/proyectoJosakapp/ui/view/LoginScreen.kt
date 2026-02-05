package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.components.cuerpoHome
import edu.josakapp.proyectoJosakapp.ui.viewmodel.LoginViewModel

@Composable
fun HomeScreen(
    onGoSecondScreen: (User) -> Unit,   // ← ahora recibe un User completo
    onGoRegisterScreen: () -> Unit,
    onGoForgotPasswordScreen: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.uiState
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                    name = state.name,
                    pass = state.pass,
                    onNameChange = viewModel::onNameChange,
                    onPassChange = viewModel::onPassChange,
                    onGoSecondScreen = {
                        viewModel.login(
                            onSuccess = { user ->
                                errorMessage = null
                                onGoSecondScreen(user)   // ← enviamos el User completo
                            },
                            onError = { msg ->
                                errorMessage = msg
                            }
                        )
                    },
                    onGoRegisterScreen = onGoRegisterScreen,
                    onGoForgotPasswordScreen = onGoForgotPasswordScreen
                )
            }

            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                )
            }
        }
    }
}
