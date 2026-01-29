package edu.josakapp.proyectoJosakapp.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.josakapp.proyectoJosakapp.ui.navigation.NavScreens
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModel

@Composable
fun MainContainerScreen() {

    val bottomNavController = rememberNavController()

    // ViewModels compartidos dentro del contenedor
    val habitosViewModel: HabitosViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavHabitoScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavHabitoScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
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

                NavigationBarItem(
                    selected = currentRoute == NavScreens.NavAjusteScreen.ruta,
                    onClick = {
                        bottomNavController.navigate(NavScreens.NavAjusteScreen.ruta) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Ajustes") },
                    label = { Text("Ajustes") }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = NavScreens.NavHabitoScreen.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(NavScreens.NavHabitoScreen.ruta) {
                HabitoScreen(habitosViewModel)
            }

            composable(NavScreens.NavRankingScreen.ruta) {
                RankingScreen(rankingViewModel)
            }

            composable(NavScreens.NavTiendaScreen.ruta) {
                Text("Tienda Screen")
            }

            composable(NavScreens.NavPinguinoScreen.ruta) {
                Text("Pingüino Screen")
            }

            composable(NavScreens.NavAjusteScreen.ruta) {
                Text("Ajuste")
            }
        }
    }
}
