package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**Clase encargada de la relación entre usuario y hábito
 * en la documentación esta puesto como N:N , esto no tiene mucho sentiod
 * asi que se cambio a 1:N*/

data class UserWithHabito(
    @Embedded val user: User,
    @Relation(
        parentColumn ="id_usuario",
        entityColumn = "id_usuario"
    )
    val habito: List<Habito>
)
