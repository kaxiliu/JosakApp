package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**Clase nueva implementada en la cual se verá
 * la relación entre pinguino y accesorio */
data class PinguinoWithAccesorios(
    @Embedded val pinguino: Pinguino,
    @Relation(
        parentColumn = "idPinguino",
        entityColumn = "id_accesorio",
        associateBy = Junction(
            value = PinguinoAccesoriosCrossRef::class,
            parentColumn = "idPinguino",
            entityColumn = "id_accesorio"
        )
    )
    val accesorios: List<Accesorios>
)
