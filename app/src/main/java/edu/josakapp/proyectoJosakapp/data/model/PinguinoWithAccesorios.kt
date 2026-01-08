package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**Clase nueva implementada en la cual se verá
 * la relación entre pinguino y accesorio */
data class PinguinoWithAccesorios(
    @Embedded val pinguino: Pinguino,
    @Relation(
        parentColumn = "id_pinguino",
        entityColumn = "id_accesorio",
        /**Aqui añadimos la nueva relación en la cual
         * se sabrá que accesorio porta el pinguino pero
         * solo de 1*/
        associateBy = Junction(PinguinoAccesoriosCrossRef::class)
    )
    val accesorios: List<Accesorios>
)
