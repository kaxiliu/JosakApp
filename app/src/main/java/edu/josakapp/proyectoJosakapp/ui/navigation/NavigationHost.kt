package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen
import edu.josakapp.proyectoJosakapp.ui.view.RegisterScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    userViewModel: UserViewModel
) {

    NavHost(
        navController = navController,
        startDestination = NavScreens.NavMainScreen.ruta
    ) {

        /** LOGIN */
        composable(NavScreens.NavMainScreen.ruta) {
            HomeScreen(
                onGoSecondScreen = { user ->
                    // Guardamos el usuario globalmente
                    userViewModel.setUser(user)

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
                onRegisterSuccess = { user ->
                    userViewModel.setUser(user)
                    navController.navigate(NavScreens.NavSecondScreen.ruta) {
                        popUpTo(NavScreens.NavMainScreen.ruta) { inclusive = true }
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }


        /** FORGOT PASSWORD */
        composable(NavScreens.NavForgotPasswordScreen.ruta) {
            ForgotPasswordScreen { }
        }

        /** MAIN CONTAINER (lee el User desde UserViewModel) */
        composable(NavScreens.NavSecondScreen.ruta) {

            val user = userViewModel.user.collectAsState().value

            if (user != null) {
                MainContainerScreen(user)
            }
        }
    }
}
