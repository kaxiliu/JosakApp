package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {

    // LOGIN
    object NavMainScreen : NavScreens("main")

    // BOTTOM NAVIGATION
    object NavHabitoScreen : NavScreens("habito")
    object NavRankingScreen : NavScreens("ranking")
    object NavTiendaScreen : NavScreens("tienda")
    object NavPinguinoScreen : NavScreens("pinguino")
    object NavAjusteScreen : NavScreens("ajuste")

    // CONTENEDOR PRINCIPAL (BottomBar)
    object NavMainContainerScreen : NavScreens("main_container")

    // OTRAS PANTALLAS
    object NavSecondScreen : NavScreens("second")

    // REGISTER & FORGOT PASSWORD
    object NavRegisterScreen : NavScreens("register")
    object NavForgotPasswordScreen : NavScreens("forgot_password")
}
