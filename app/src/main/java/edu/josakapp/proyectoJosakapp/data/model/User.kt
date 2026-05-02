package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**Clase que representará la tabla de el usuario */

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id_usuario: Int = 0,
    val uid: String = "", //UID generado por Firebase Authentication
    val nombre_usuario: String,
    val email: String,
    val contrasena: String,
    val esPremium: Boolean, /*Variable cambiada, tipo_usuario**/
    val monedas: Int,
    val fecha_registro: Long,
    val xp_total: Int,
    val telefono: Int,
    val fotoPerfil: String, /**Variable nueva añadida*/
    val nivel:Int, //Nivel de Ranking
    val puntos:Int
){
    companion object {
        //Calcula el nivel del usuario basado en su XP total.
        fun calculateLevel(xp: Int): Int {
            if (xp <= 0) return 1
            val level = Math.pow((xp / 100.0), 1.0 / 1.5).toInt()
            return if (level < 1) 1 else level
        }

        // Calcula el XP requerido para alcanzar el siguiente nivel.
        fun getRequiredXpForNextLevel(level: Int): Int {
            return (100 * Math.pow(level.toDouble(), 1.5)).toInt()
        }
        // Calcula el progreso del nivel actual como un porcentaje
        fun calculateLevelProgress(xpActual: Int, level: Int): Float {
            val requiredXp = getRequiredXpForNextLevel(level)
            //Evitamos división por cero
            val currentLevelXp = xpActual % requiredXp
            return (currentLevelXp.toFloat() / requiredXp.toFloat()).coerceIn(0f, 1f)
        }

    }
}