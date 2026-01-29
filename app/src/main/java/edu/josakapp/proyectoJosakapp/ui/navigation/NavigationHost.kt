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


@Composable
fun NavigationHost(navController: NavHostController) {

    val vm: HabitosViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavScreens.NavMainScreen.ruta
    ) {

        // LOGIN
        composable(NavScreens.NavMainScreen.ruta) {
            HomeScreen(
                name = vm.name,
                onNameChange = vm::updateName,
                onGoSecondScreen = {
                    navController.navigate(NavScreens.NavMainContainerScreen.ruta) {
                        popUpTo(NavScreens.NavMainScreen.ruta) { inclusive = true }
                    }
                },
                onGoRegisterScreen = {
                    navController.navigate(NavScreens.NavRegisterScreen.ruta)
                },
                onGoForgotPasswordScreen = {
                    navController.navigate(NavScreens.NavForgotPasswordScreen.ruta)
                }
            )
        }

        // REGISTER
        composable(NavScreens.NavRegisterScreen.ruta) {
            RegisterScreen()
        }

        // FORGOT PASSWORD
        composable(NavScreens.NavForgotPasswordScreen.ruta) {
            ForgotPasswordScreen()
        }

        // MAIN CONTAINER (Bottom Navigation)
        composable(NavScreens.NavMainContainerScreen.ruta) {
            MainContainerScreen()
        }
    }
}
