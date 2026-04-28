package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.ThemeViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.UserViewModel


/**Este acuta como segundo NAvHost **/
@Composable
fun MainContainerScreen(user: User, themeViewModel: ThemeViewModel,
                        userViewModel: UserViewModel) {

    val bottomNavController = rememberNavController()

    val habitosViewModel: HabitosViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    //val userViewModel: UserViewModel=viewModel()
    // Cargar el usuario actual en el UserViewModel
    //val currentUser by userViewModel.user.collectAsState()
    val currentUserState by userViewModel.user.collectAsState()
    val activeUser = currentUserState ?: user

    LaunchedEffect(user.id_usuario) {
        userViewModel.loadUser(user.id_usuario)
    }

    LaunchedEffect(habitosViewModel) {
        habitosViewModel.userXpUpdated.collect { userId ->
            userViewModel.refreshCurrentUser(userId)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavHabitoScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavHabitoScreen.ruta) {
                            popUpTo(NavScreens.NavHabitoScreen.ruta) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Hábitos") },
                    label = { Text("Hábitos") }
                )

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavRankingScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavRankingScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Ranking") },
                    label = { Text("Ranking") }
                )

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavTiendaScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavTiendaScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Tienda") },
                    label = { Text("Tienda") }
                )

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavPinguinoScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavPinguinoScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Face, contentDescription = "Pingüino") },
                    label = { Text("Pingüino") }
                )

                // Navegado a perfil

                // En tu NavigationBar dentro de MainContainerScreen
                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavPerfilScreen.ruta, // Ahora la ruta principal es Perfil
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavPerfilScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") } // Cambiamos "Ajustes" por "Perfil"
                )
                /*NavigationBarItem(
                    selected = currentRoute == NavScreens.NavAjusteScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavAjusteScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Ajustes") },
                    label = { Text("Ajustes") }*
                )*/
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = NavScreens.NavHabitoScreen.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(NavScreens.NavHabitoScreen.ruta) {
                HabitoScreen(
                    habitosViewModel,
                    userViewModel,
                    userId = activeUser.id_usuario,
                    navController = bottomNavController
                )

            }

            composable("stats") {
                val registros by habitosViewModel.todosLosRegistros.collectAsState()
                StatsScreen(
                    navController = bottomNavController,
                    registros = registros,
                    userViewModel = userViewModel
                )

            }

            composable("money") {
                MoneyScreen(
                    navController = bottomNavController,
                    user = listOf(activeUser)
                )
            }

            composable(NavScreens.NavRankingScreen.ruta) {
                RankingScreen(rankingViewModel)
            }

            composable(NavScreens.NavTiendaScreen.ruta) {
                // TiendaScreen(user)
                TiendaScreen(user=activeUser)

            }

            composable(NavScreens.NavPinguinoScreen.ruta) {
                Text("Pingüino Screen")
            }

            composable(NavScreens.NavAjusteScreen.ruta) {
                SettingsScreen(
                    onNavigate = { ruta ->
                        bottomNavController.navigate(ruta)
                    },
                    onBack = {
                        bottomNavController.popBackStack()
                    }
                )
            }
            composable(NavScreens.NavPerfilScreen.ruta) {
                PerfilScreen(
                    user = user,
                    onNavigateToSettings = {
                        bottomNavController.navigate(NavScreens.NavAjusteScreen.ruta)
                    },
                    onCompleteProfile = {bottomNavController.navigate(NavScreens.NavCompletarPerfil.ruta)}
                )
            }
            composable(NavScreens.NavCompletarPerfil.ruta) {
                CompletarPerfilScreen(
                    user = user,
                    onBack = { bottomNavController.popBackStack() }
                )
            }
            composable(NavScreens.NavPreferenciasScreen.ruta) {
                PreferenciasScreen(
                    onBack = { bottomNavController.popBackStack() },
                    themeViewModel = themeViewModel
                )
            }
            composable(NavScreens.NavNotificacionesScreen.ruta) {
                NotificacionesScreen(
                    onBack = { bottomNavController.popBackStack() },
                    onNavigate = { ruta -> bottomNavController.navigate(ruta) }
                )
            }
            composable(NavScreens.NavRecordatorioScreen.ruta) {
                RecordatorioScreen(onBack = { bottomNavController.popBackStack() })
            }
            // Sub Menu de amigos
            composable(NavScreens.NavAmigosScreen.ruta) {
                SubMenuAmigosScreen(
                    onBack = { bottomNavController.popBackStack() }
                )
            }
            // CLASIFICACIÓN
            composable(NavScreens.NavClasificacion.ruta) {
                ClasificacionNotificacionesScreen(onBack = { bottomNavController.popBackStack() })
            }
            // Anuncios
            composable(NavScreens.NavAnuncioScreen.ruta) { // Asegúrate que NavAnunciosScreen existe en tu objeto NavScreens
                AnunciosScreen(onBack = { bottomNavController.popBackStack() })
            }
            composable(NavScreens.NavConfiguracionPrivacidadScreen.ruta) {
                PrivacidadScreen(onBack = { bottomNavController.popBackStack() })
            }
        }
    }
}
