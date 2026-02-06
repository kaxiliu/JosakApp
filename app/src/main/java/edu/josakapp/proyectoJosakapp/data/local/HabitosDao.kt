package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import kotlinx.coroutines.flow.Flow




@Dao
interface HabitosDao {

    //Obtener todos habitos (solo admin)
    @Query("SELECT * FROM habito ORDER BY nombre ASC")
    fun getAllHabitos(): Flow<List<Habito>>

    //Obtener un hábito por ID
    @Query("SELECT * FROM habito WHERE id_habito = :id")
    suspend fun getHabitoById(id: Int): Habito?

    //Obtener todos los hábitos de un usuario
    @Query("SELECT * FROM habito WHERE id_usuario = :userId")
    fun getHabitosByUserId(userId: Int): Flow<List<Habito>>

    //Insertar hábito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: Habito): Long

    //Actualizar estado del hábito
    @Query("UPDATE habito SET estado = :estado WHERE id_habito = :id")
    suspend fun updateEstado(id: Int, estado: Boolean)

    //Actualizar hábito completo
    @Update
    suspend fun updateHabito(habito: Habito): Int

    //Eliminar hábito
    @Delete
    suspend fun deleteHabito(habito: Habito): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistro(registro: HabitoRegistro)

    @Query("DELETE FROM habito_registro WHERE id_habito = :idHabito AND fecha = :fecha")
    suspend fun deleteRegistro(idHabito: Int, fecha: Long)


    @Query("SELECT COUNT(*) > 0 FROM habito_registro WHERE id_habito = :idHabito AND fecha = :fecha")
    fun isHabitoCompletado(idHabito: Int, fecha: Long): Flow<Boolean>

    @Query("SELECT * FROM habito_registro")
    fun getAllRegistros(): Flow<List<HabitoRegistro>>
}
