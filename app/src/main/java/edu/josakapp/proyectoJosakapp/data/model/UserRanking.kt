package edu.josakapp.proyectoJosakapp.data.model

data class UserRanking(
    val id_usuario: Int = 0,
    val nombre_usuario: String,
    val puntos: Int,
    val nivel: Int,
    val fotoPerfil: String
)
