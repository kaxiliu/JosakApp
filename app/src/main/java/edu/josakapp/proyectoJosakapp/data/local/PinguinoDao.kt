package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.*
import edu.josakapp.proyectoJosakapp.data.model.Pinguino
import edu.josakapp.proyectoJosakapp.data.model.PinguinoAccesoriosCrossRef
import edu.josakapp.proyectoJosakapp.data.model.PinguinoWithAccesorios

@Dao
interface PinguinoDao {
    // Obtene el pingüino asociado a un usuario por su ID de usuario
    @Query("SELECT * FROM pinguino WHERE id_usuario = :userId LIMIT 1")
    suspend fun getPinguinoByUserId(userId: Int): Pinguino?

    // Inserta o actualiza un pingüino asociado a un usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPinguino(pinguino: Pinguino)

    // Inserta o actualiza la relación entre un pingüino y un accesorio (relación N-N)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPinguinoAccesorio(crossRef: PinguinoAccesoriosCrossRef)

    // Busca un pingüino con sus accesorios asociados por el ID de usuario
    @Transaction
    @Query("SELECT * FROM pinguino WHERE id_usuario = :userId LIMIT 1")
    suspend fun getPinguinoWithAccesorios(userId: Int): PinguinoWithAccesorios?
}