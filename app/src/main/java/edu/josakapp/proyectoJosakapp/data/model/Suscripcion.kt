package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**Clase encargada de la suscripcion*/
@Entity(tableName = "suscripcion")
data class Suscripcion(
    @PrimaryKey(autoGenerate = true) val id_suscripcion: Int = 0,
    val fecha_inicio: Long,
    val fecha_fin: Long,
    val estado: Boolean,
    val id_usuario: Int
)
