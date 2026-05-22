package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.josakapp.proyectoJosakapp.data.model.Pinguino
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.model.UserWithHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Obtiene todos los usuarios con sus hábitos (relación 1-N)
    @Transaction
    @Query("SELECT * FROM User ORDER BY nombre_usuario")
    fun getUsersWithHabitos(): Flow<List<UserWithHabito>>

    // Obtiene un usuario por su ID
    @Query("SELECT * FROM User WHERE id_usuario = :id")
    suspend fun getUserById(id: Int): User?

    // Inserta o actualiza un usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    //Obtene un usuario por su UID (identificador único de Firebase Authentication)
    @Query("SELECT * FROM User WHERE uid = :uid LIMIT 1")
    suspend fun getUserByUid(uid: String): User?

    // Actualiza el XP total y el nivel de un usuario
    @Query("UPDATE user SET xp_total = :xp, nivel = :level WHERE id_usuario = :userId")
    suspend fun updateUserXpAndLevel(userId: Int, xp: Int, level: Int)

    // Actualiza los puntos de un usuario
    @Query("UPDATE user SET xp_total = :xp, nivel = :level, puntos = :puntos WHERE id_usuario = :userId")
    suspend fun updateUserXpLevelAndPuntos(userId: Int, xp: Int, level: Int, puntos: Int)

}
