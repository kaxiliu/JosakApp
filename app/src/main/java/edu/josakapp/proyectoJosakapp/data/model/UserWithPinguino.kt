package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**Clase encargada de la relación entre
 * usuario y pinguino 1:1
 * Por el momento solo un usuario podra tener un pinguino
 * de el cual , con el tiempo se sacará una media
 * de los hábitos y de acuerdo a ello el pinguino reaccionará*/
data class UserWithPinguino(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id_usuario",
        entityColumn = "id_usuario",

    )
    val pinguino: Pinguino
)
