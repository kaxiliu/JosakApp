package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity

/**Clase que representara la relación 1:N entre
 * el pinguino y solo un accesorio*/
@Entity(
    tableName = "PinguinoAccesoriosCrossRef",
    primaryKeys = ["idPinguino", "id_accesorio"]
)
data class PinguinoAccesoriosCrossRef(
    val idPinguino: Int,
    val id_accesorio: Int
)
