package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**Clase encargada de los accesorios del pinguino*/
@Entity(tableName = "accesorios")
data class Accesorios(
    @PrimaryKey(autoGenerate = true)
    val id_accesorio: Int = 0,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val precio: Float = 0f,
    val tipo: Boolean = false,
    val id_pinguino: Int /**Variable que referencia al pinguino*/
)
