package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**Clase que se encargará de la relación entre usuarios
 * y suscripciones*/
data class UserWithSuscripcion(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id_usuario",
        entityColumn = "id_usuario"
    )
    val suscripcion: List<Suscripcion>
)
