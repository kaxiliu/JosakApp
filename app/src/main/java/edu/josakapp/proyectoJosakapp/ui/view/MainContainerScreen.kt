package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.PinguinoViewModel
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
    val pinguinoViewModel: PinguinoViewModel = viewModel()

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
                    viewModel = habitosViewModel,
                    userViewModel = userViewModel,
                    userId = activeUser.id_usuario,
                    navController = bottomNavController
                )
            }

            composable("stats") {
                val registros by habitosViewModel.todosLosRegistros.collectAsState()
                StatsScreen(
                    navController = bottomNavController,
                    registros = registros,
                    userViewModel = userViewModel,
                    habitosViewModel=habitosViewModel
                )

            }

            composable("money") {
                MoneyScreen(
                    navController = bottomNavController,
                    userViewModel = userViewModel
                )
            }

            composable(NavScreens.NavRankingScreen.ruta) {
                RankingScreen(rankingViewModel, userViewModel, bottomNavController)
            }

            composable(NavScreens.NavTiendaScreen.ruta) {
                // Tienda usando UserViewModel
                TiendaScreen(userViewModel = userViewModel)

            }

            composable(NavScreens.NavPinguinoScreen.ruta) {
                //Text("Pingüino Screen")
                // Reemplazamos el Text por el componente real
                PinguinoScreen(
                    userViewModel = userViewModel,
                    habitosViewModel = habitosViewModel,
                    pinguinoViewModel = pinguinoViewModel,
                    onBack = {
                        bottomNavController.popBackStack()
                    }
                )
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
                    user = activeUser,
                    userViewModel = userViewModel,
                    onNavigateToSettings = {
                        bottomNavController.navigate(NavScreens.NavAjusteScreen.ruta)
                    },
                    onCompleteProfile = {bottomNavController.navigate(NavScreens.NavCompletarPerfil.ruta)},
                    onNavigateToSearch = {bottomNavController.navigate(NavScreens.NavBuscarAmigosSreen.ruta)}
                )
            }
            composable("perfil_user/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
                val targetUserState = remember { mutableStateOf<User?>(null) }

                LaunchedEffect(userId) {
                    targetUserState.value = null
                    if (userId != null) {
                        targetUserState.value = try {
                            edu.josakapp.proyectoJosakapp.data.di.AppModule.userRepository.getUserById(userId)
                        } catch (_: Exception) {
                            null
                        }
                    }
                }

                val targetUser = targetUserState.value
                if (targetUser != null) {
                    PerfilScreen(
                        user = targetUser,
                        userViewModel = userViewModel,
                        onNavigateToSettings = {
                            bottomNavController.navigate(NavScreens.NavAjusteScreen.ruta)
                        },
                        onCompleteProfile = { bottomNavController.navigate(NavScreens.NavCompletarPerfil.ruta) },
                        onNavigateToSearch = { bottomNavController.navigate(NavScreens.NavBuscarAmigosSreen.ruta) }
                    )
                } else {
                    Text("Cargando perfil...")
                }
            }
            composable(NavScreens.NavBuscarAmigosSreen.ruta) {
                BuscarAmigosScreen(
                    userViewModel = userViewModel,
                    miId = activeUser.uid.ifBlank { activeUser.id_usuario.toString() },
                    onBack = { bottomNavController.popBackStack()}
                )
            }

            composable(NavScreens.NavCompletarPerfil.ruta) {
                CompletarPerfilScreen(
                    user = user,
                    userViewModel = userViewModel,
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