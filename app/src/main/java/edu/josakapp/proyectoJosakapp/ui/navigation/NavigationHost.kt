package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen
import edu.josakapp.proyectoJosakapp.ui.view.RegisterScreen
import edu.josakapp.proyectoJosakapp.ui.view.SettingsScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.ThemeViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    themeViewModel: ThemeViewModel
) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        val  currentUser = auth.currentUser
        if(currentUser != null) {
            // El usuario ya está logueado en Firebase, cargamos sus datos de la DB
            // Aquí llamarías a tu repositorio para obtener el objeto User completo
            userViewModel.loadUserFromId(currentUser.uid) { user ->
                if (user != null) {
                    navController.navigate(NavScreens.NavSecondScreen.ruta) {
                        popUpTo(NavScreens.NavMainScreen.ruta) { inclusive = true }
                    }
                }
            }
        }

    }
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

        /** MAIN CONTAINER */
        composable(NavScreens.NavSecondScreen.ruta) {
            val user = userViewModel.user.collectAsState().value

            if (user != null) {
                MainContainerScreen(user = user, themeViewModel = themeViewModel,
                    userViewModel = userViewModel)
            } else {
                // Si por alguna razón el usuario es nulo, vuelve al login
                navController.navigate(NavScreens.NavMainScreen.ruta)
            }
        }

        /** PANTALLA AJUSTE PRINCIPAL */
        composable(NavScreens.NavAjusteScreen.ruta) {
            SettingsScreen(
                onNavigate = {ruta -> navController.navigate(ruta)},
                onBack = {navController.popBackStack() }
            )
        }

        /**SUBPANTALLAS (Perfil) */
        composable(NavScreens.NavPerfilScreen.ruta) {
            SettingsScaffold(title = "PERFIL", onBackClick = { navController.popBackStack()}) {padding ->
                Text("Pantalla perfil")
            }
        }
    }
}
