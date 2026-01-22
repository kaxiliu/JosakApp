package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    suspend fun insertUser(user: User)
}
