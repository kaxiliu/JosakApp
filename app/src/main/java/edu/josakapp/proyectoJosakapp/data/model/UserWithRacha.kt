package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**Clase encargada de la relación 1:N entre Usuario y Racha*/
data class UserWithRacha(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id_usuario", /**Siempre el id, de el embedded*/
        entityColumn = "id_usuario" /**El de la tabla racha */
    )
    val racha: List<Racha>
)
