package edu.josakapp.proyectoJosakapp.data.model

import android.text.BoringLayout
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**Clase encargada de la tabla de Habitos*/
@Entity(tableName = "habito")
data class Habito(
    @PrimaryKey(autoGenerate = true) val id_habito: Int = 0,
    val nombre: String,
    val descripcion: String,
    val exp_habito: Int,
    val frecuencia: String,
    val estado: Boolean,
    val fecha_creacion: Long,
    val icono: String, /**Nueva variable añadida */
    val id_usuario: Int
)
