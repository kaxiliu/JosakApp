package edu.josakapp.proyectoJosakapp.data.model

data class HabitoRemote(
    val id_habito: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val exp_habito: Int = 0,
    val frecuencia: String = "",
    val estado: Boolean = false,
    val fecha_creacion: Long = 0L,
    val icono: String = "",
    val colorHex: Long = 0xFFD3D3D3
)
