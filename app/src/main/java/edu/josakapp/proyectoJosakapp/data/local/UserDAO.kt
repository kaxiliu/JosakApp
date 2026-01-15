package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.model.UserWithHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    /**Para poder consultar los hábitos de un usuario en concreto*/
    @Transaction
    @Query("SELECT * FROM user ORDER BY nombre_usuario")
    fun getUserWithHabito(): List<UserWithHabito>

    @Query("SELECT * FROM habito")
    fun getAllHabito(): Flow<List<UserWithHabito>>

    @Query("SELECT * FROM user WHERE id_usuario = id_usuario")
    suspend fun getUserById(id_usuario: Int): User?

    @Query("SELECT * FROM habito WHERE id_habito = id_habito")
    suspend fun getHabitoById(id_habito: Int): Habito?


}