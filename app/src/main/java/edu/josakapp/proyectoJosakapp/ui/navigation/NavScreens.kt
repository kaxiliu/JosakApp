package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {

    object NavMainScreen : NavScreens("main")              // Login
    object NavRegisterScreen : NavScreens("register")      // Registro
    object NavForgotPasswordScreen : NavScreens("forgot_password")

    object NavSecondScreen : NavScreens("second")          // Contenedor principal

    // Bottom Navigation
    object NavHabitoScreen : NavScreens("habito")
    object NavStatsScreen : NavScreens("stats")
    object NavRankingScreen : NavScreens("ranking")
    object NavTiendaScreen : NavScreens("tienda")
    object NavPinguinoScreen : NavScreens("pinguino")
    object NavAjusteScreen : NavScreens("ajuste")
}
