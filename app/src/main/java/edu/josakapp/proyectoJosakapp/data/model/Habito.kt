package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**Clase encargada de la tabla de Habitos*/
@Entity(tableName = "habito")
data class Habito(
    @PrimaryKey(autoGenerate = true) val id_habito: Int = 0,
    val nombre: String,
    val descripcion: String,
    val exp_habito: Int,
    val frecuencia: String,
    val estado: Boolean,
    val fecha_creacion: Long,
    val icono: String,
    val id_usuario: Int,
    val colorHex: Long = 0xFFD3D3D3
)
