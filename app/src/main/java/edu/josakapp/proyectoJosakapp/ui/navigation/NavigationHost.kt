package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen
import edu.josakapp.proyectoJosakapp.ui.view.PerfilScreen
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
        /**SUBPANTALLAS (Perfil)
        composable(NavScreens.NavPerfilScreen.ruta) {
            SettingsScaffold(title = "PERFIL", onBackClick = { navController.popBackStack()}) {padding ->
                Text("Pantalla perfil")
            }
        }
        }*/
        composable(NavScreens.NavPerfilScreen.ruta) {
            val user = userViewModel. user.collectAsState().value

            if(user != null){
                PerfilScreen(
                    user = user,
                    userViewModel = userViewModel,
                    onNavigateToSettings = { navController.navigate(NavScreens.NavAjusteScreen.ruta) },
                    onCompleteProfile = { navController.navigate(NavScreens.NavCompletarPerfil.ruta) },
                    onNavigateToSearch = { navController.navigate(NavScreens.NavAmigosScreen.ruta)}
                )
            } else {
                navController.navigate(NavScreens.NavMainScreen.ruta)
            }
        }
        // Perfil de otro usuario por id
        composable("perfil_user/{userId}") { backStackEntry ->
            val userIdStr = backStackEntry.arguments?.getString("userId")
            val userId = userIdStr?.toIntOrNull()

            // Estado para el usuario objetivo (para que la UI se recomponga cuando llegue)
            val targetUserState = remember { mutableStateOf<edu.josakapp.proyectoJosakapp.data.model.User?>(null) }

            LaunchedEffect(userId) {
                targetUserState.value = null
                if (userId != null) {
                    try {
                        val u = edu.josakapp.proyectoJosakapp.data.di.AppModule.userRepository.getUserById(userId)
                        targetUserState.value = u
                    } catch (_: Exception) {
                        targetUserState.value = null
                    }
                }
            }

            val targetUser = targetUserState.value
            if (targetUser != null) {
                PerfilScreen(
                    user = targetUser,
                    userViewModel = userViewModel,
                    onNavigateToSettings = { navController.navigate(NavScreens.NavAjusteScreen.ruta) },
                    onCompleteProfile = { navController.navigate(NavScreens.NavCompletarPerfil.ruta) },
                    onNavigateToSearch = { navController.navigate(NavScreens.NavAmigosScreen.ruta)}
                )
            } else {
                // Mientras se carga, mostrar placeholder
                Text("Cargando perfil...")
            }
        }
    }
}
