package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**Clase que representará la tabla de el usuario */

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id_usuario: Int = 0,
    val nombre_usuario: String,
    val email: String,
    val contrasena: String,
    val esPremium: Boolean, /*Variable cambiada, tipo_usuario**/
    val monedas: Int,
    val fecha_registro: Date,
    val xp_total: Int,
    val telefono: Int,
    val fotoPerfil: String, /**Variable nueva añadida*/
)