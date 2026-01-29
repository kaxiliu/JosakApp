package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.RegisterScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen

@Composable
fun NavigationHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavScreens.NavMainScreen.ruta
    ) {

        /** LOGIN */
        composable(NavScreens.NavMainScreen.ruta) {
            HomeScreen(
                onGoSecondScreen = {
                    navController.navigate(NavScreens.NavSecondScreen.ruta) {
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

        /** REGISTER */
        composable(NavScreens.NavRegisterScreen.ruta) {
            RegisterScreen(
                onRegister = { _, _, _ -> },
                onGoLogin = { navController.popBackStack() }
            )
        }

        /** FORGOT PASSWORD */
        composable(NavScreens.NavForgotPasswordScreen.ruta) {
            ForgotPasswordScreen { }
        }

        /** MAIN CONTAINER */
        composable(NavScreens.NavSecondScreen.ruta) {
            MainContainerScreen()
        }
    }
}
