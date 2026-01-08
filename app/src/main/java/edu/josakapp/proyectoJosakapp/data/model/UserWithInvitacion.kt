package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**Clase encargada de la relacion entre usuarios e invitacion*/
data class UserWithInvitacion(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id_usuario",
        entityColumn = "id_usuario_emisor"
    )
    val invitacion: List<Invitacion>
)
