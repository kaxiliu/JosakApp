package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**Clase encargada de la tabla de rachas */
@Entity(tableName = "racha")
data class Racha(
    @PrimaryKey(autoGenerate = true) val idRacha: Int = 0,
    val dias_consecutivos: Int,
    val fecha_ultimo_dia: Long,
    val fecha_inicio: Long,
    val idUser: Int
)
