package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.RegisterScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel


@Composable
fun NavigationHost(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel()) {

    val vm: HabitosViewModel = viewModel()
    val context = LocalContext.current

    /**Se hace esto para que compruebe que si el usuario no esta logeado , que vaya a registrarse, y
     * si si esta que se diriga a la segunda pantalla*/
    val inicio = if ( userViewModel.isUserLogged()) {
        "main_container"
    } else {
        NavScreens.NavMainScreen.ruta
    }
    NavHost(
        navController = navController,
        startDestination = inicio
    ) {
        composable (
            NavScreens.NavMainScreen.ruta
        ){
            HomeScreen(
                name = vm.name,
                onNameChange = vm::updateName,
                onGoSecondScreen = {navController.navigate("main_container")},
                onGoRegisterScreen = {navController.navigate(NavScreens.NavRegisterScreen.ruta)},
                onGoForgotPasswordScreen = {navController.navigate(NavScreens.NavForgotPasswordScreen.ruta)}

                /**Creo que aqui tiene que tener botone de registrarse y recuperar contraseña*/
            )
        }
        /**REGISTER */
        composable(NavScreens.NavRegisterScreen.ruta) {
            RegisterScreen()
        }
        /**PASSWORD FORGOT*/
        composable(NavScreens.NavForgotPasswordScreen.ruta) {
            ForgotPasswordScreen()
        }
        composable("main_container") {
            MainContainerScreen()
        }
        /*
        composable ( NavScreens.NavHabitoScreen.ruta ) {
            val productos = vm.productos
            HabitoScreen()
        }*/
    }
}

