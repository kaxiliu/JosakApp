package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.model.UserWithHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitosDao {
    // Consultas que devuelven un FLOW

    // Devuelve un Flow con la lista de todas las marcas
    // ordenadas por nombre
    @Query("SELECT * FROM Habito ORDER BY nombre")
    fun getAllHabitos(): Flow<List<Habito>>

    // Devuelve un Flow con la lista de coches junto con sus marcas,
    // ordenados por el modelo del coche
    @Transaction  // Permite obtener datos de varias tablas relacionadas con una sola consulta.
    @Query("SELECT * FROM Habito ORDER BY id_usuario")
    fun getUsersWithHabitos(): Flow<List<UserWithHabito>>

    //Devuelve un Flow con un coche específico por su ID
    @Query("SELECT * FROM Habito WHERE id_habito = :id")
    fun getHabitoById(id: Int): Flow<Habito?>
    @Query("SELECT * FROM Habito WHERE id_usuario = :id")
    fun getUserById(id: Int): Flow<Habito?>


    //Inserta una nueva usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    //Inserta un nuevo habito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: Habito)

    //Elimina un habito
    @Delete
    suspend fun deleteHabito(habito: Habito):Int

}