package edu.josakapp.proyectoJosakapp.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.josakapp.proyectoJosakapp.data.local.HabitosDao
import edu.josakapp.proyectoJosakapp.data.local.UserDao
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User

@Database(
    entities = [Habito::class, User::class],
    version = 1,
    exportSchema = true // Importante para migraciones
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun habitosDAO(): HabitosDao // Conexión con DAO de Habito
    abstract fun usersDAO(): UserDao // Conexión con DAO de Usuario

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?:
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitos.db"
                ).fallbackToDestructiveMigration() // Solo en desarrollo.
                    .build()

                INSTANCE = instance // Asigna la instancia a la variable volátil.
                instance // Devuelve la instancia de la base de datos.
            }
        }
    }
}