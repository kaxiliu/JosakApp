package edu.josakapp.proyectoJosakapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.josakapp.proyectoJosakapp.data.model.Amigo
import kotlinx.coroutines.flow.Flow

@Dao
interface AmigosDao {

    @Query("SELECT * FROM amigos")
    fun getAmigos(): Flow<List<Amigo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAmigo(amigo: Amigo)
}
