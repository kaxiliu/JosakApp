package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.UserWithHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitosDao {

    // Obtiene todos los hábitos ordenados por nombre
    @Query("SELECT * FROM Habito ORDER BY nombre")
    fun getAllHabitos(): Flow<List<Habito>>

    // Obtiene todos los usuarios con sus hábitos (relación 1-N)
    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithHabitos(): Flow<List<UserWithHabito>>

    // Obtiene un hábito por su ID
    @Query("SELECT * FROM Habito WHERE id_habito = :id")
    fun getHabitoById(id: Int): Flow<Habito?>

    // Obtiene todos los hábitos de un usuario concreto
    @Query("SELECT * FROM Habito WHERE id_usuario = :userId")
    fun getHabitosByUserId(userId: Int): Flow<List<Habito>>

    // Inserta o actualiza un hábito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: Habito)

    // Elimina un hábito
    @Delete
    suspend fun deleteHabito(habito: Habito): Int
}
