package edu.josakapp.proyectoJosakapp.ui.navigation

sealed class NavScreens(val ruta: String) {

    object NavMainScreen : NavScreens("main")              // Login
    object NavRegisterScreen : NavScreens("register")      // Registro
    object NavForgotPasswordScreen : NavScreens("forgot_password")

    object NavSecondScreen : NavScreens("second")          // Contenedor principal (recibirá UID)

    // Bottom Navigation
    object NavHabitoScreen : NavScreens("habito")
    object NavStatsScreen : NavScreens("stats")
    object NavMoneyScreen : NavScreens("money")
    object NavRankingScreen : NavScreens("ranking")
    object NavTiendaScreen : NavScreens("tienda")
    object NavPinguinoScreen : NavScreens("pinguino")
    object NavAjusteScreen : NavScreens("ajuste")

    /**Navegación para la pantalla de ajustes*/
    object NavPerfilScreen : NavScreens("sub_perfil")
    object NavCompletarPerfil: NavScreens("sub_completar_perfil")
    object NavPreferenciasScreen : NavScreens("sub_preferencias")
    object NavNotificacionesScreen : NavScreens("sub_notificaciones")
    object NavConfiguracionPrivacidadScreen : NavScreens("sub_configuracion_privacidad")

    /**Pantallas de la parte de notificaciones*/
    object NavRecordatorioScreen : NavScreens("recordatorio")
    object NavAmigosScreen : NavScreens("amigos")
    object NavClasificacion: NavScreens("clasificacion")
    object NavAnuncioScreen: NavScreens("Anuncio")
    object NavBuscarAmigosSreen: NavScreens("buscar_amigos")
}
