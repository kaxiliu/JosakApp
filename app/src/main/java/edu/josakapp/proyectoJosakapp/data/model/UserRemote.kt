package edu.josakapp.proyectoJosakapp.data.model

data class UserRemote(
    val uid: String = "",
    val nombre_usuario: String = "",
    val email: String = "",
    val fotoPerfil: String = "",
    val nivel: Int = 1,
    val puntos: Int = 0,
    val monedas: Int = 0,
    val esPremium: Boolean = false,
    val fecha_registro: Long = System.currentTimeMillis(),
    val xp_total: Int = 0,
    val telefono: Int = 0
)
