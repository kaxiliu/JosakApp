package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**Clase encargada de la tabla de pinguinos*/
@Entity(tableName = "pinguino")
data class Pinguino(
    @PrimaryKey(autoGenerate = true) val idPinguino: Int = 0,
    val nivel : Int,
    val xp_actual: Int,
    val estado: Boolean,
    val nombre: String,
    val id_usuario: Int
)
