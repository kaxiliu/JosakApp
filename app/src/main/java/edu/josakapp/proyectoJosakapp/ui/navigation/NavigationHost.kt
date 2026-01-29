package edu.josakapp.proyectoJosakapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.ui.view.ForgotPasswordScreen
import edu.josakapp.proyectoJosakapp.ui.view.HomeScreen
import edu.josakapp.proyectoJosakapp.ui.view.RegisterScreen
import edu.josakapp.proyectoJosakapp.ui.view.MainContainerScreen
import edu.josakapp.proyectoJosakapp.ui.view.RankingScreen
import edu.josakapp.proyectoJosakapp.ui.viewmodel.HabitosViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModel
import edu.josakapp.proyectoJosakapp.ui.viewmodel.RankingViewModelFactory

@Composable
fun NavigationHost(navController: NavHostController) {

    val context = LocalContext.current

    // Base de datos
    val database = AppDatabase.getInstance(context)

    // ÚNICO datasource con todos los DAOs
    val localDatasource = LocalDatasource(
        userDao = database.usersDAO(),
        habitosDao = database.habitosDAO(),
        amigosDao = database.amigosDAO()
    )

    // ViewModels
    val habitosVM: HabitosViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = NavScreens.NavMainScreen.ruta
    ) {

        /** HOME */
        composable(NavScreens.NavMainScreen.ruta) {
            HomeScreen(
                name = habitosVM.name,
                onNameChange = habitosVM::updateName,
                onGoSecondScreen = { navController.navigate("main_container") },
                onGoRegisterScreen = { navController.navigate(NavScreens.NavRegisterScreen.ruta) },
                onGoForgotPasswordScreen = { navController.navigate(NavScreens.NavForgotPasswordScreen.ruta) }
            )
        }

        /** REGISTER */
        composable(NavScreens.NavRegisterScreen.ruta) {
            RegisterScreen(
                onRegister = { name, email, password ->
                    // Lógica de registro
                },
                onGoLogin = {
                    navController.popBackStack()
                }
            )
        }

        /** FORGOT PASSWORD */
        composable(NavScreens.NavForgotPasswordScreen.ruta) {
            ForgotPasswordScreen { email ->
                // Lógica recuperación
            }
        }

        /** MAIN CONTAINER */
        composable("main_container") {
            MainContainerScreen()
        }

        /** RANKING */
        composable("ranking") {

            // Crear RankingViewModel con factory
            val rankingVM: RankingViewModel = viewModel(
                factory = RankingViewModelFactory(localDatasource)
            )

            RankingScreen(viewModel = rankingVM)
        }
    }
}
