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

    // Obtener todos los hábitos
    @Query("SELECT * FROM Habito ORDER BY nombre")
    fun getAllHabitos(): Flow<List<Habito>>

    // Obtener usuarios con sus hábitos (relación 1-N)
    @Transaction
    @Query("SELECT * FROM User ORDER BY nombre_usuario")
    fun getUsersWithHabitos(): Flow<List<UserWithHabito>>

    // Obtener un hábito por ID
    @Query("SELECT * FROM Habito WHERE id_habito = :id")
    fun getHabitoById(id: Int): Flow<Habito?>

    // Obtener todos los hábitos de un usuario
    @Query("SELECT * FROM Habito WHERE id_usuario = :userId")
    fun getHabitosByUserId(userId: Int): Flow<List<Habito>>

    // Insertar hábito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: Habito)

    // Eliminar hábito
    @Delete
    suspend fun deleteHabito(habito: Habito): Int
}
