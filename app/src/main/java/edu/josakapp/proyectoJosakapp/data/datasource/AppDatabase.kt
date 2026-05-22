package edu.josakapp.proyectoJosakapp.data.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.josakapp.proyectoJosakapp.data.local.AmigosDao
import edu.josakapp.proyectoJosakapp.data.local.HabitosDao
import edu.josakapp.proyectoJosakapp.data.local.PinguinoDao
import edu.josakapp.proyectoJosakapp.data.local.UserDao
import edu.josakapp.proyectoJosakapp.data.model.Accesorios
import edu.josakapp.proyectoJosakapp.data.model.Amigo
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.model.Pinguino
import edu.josakapp.proyectoJosakapp.data.model.PinguinoAccesoriosCrossRef
import edu.josakapp.proyectoJosakapp.data.model.User

@Database(
    entities = [
        Habito::class,
        User::class,
        Amigo::class,
        HabitoRegistro::class,
        Pinguino::class,
        Accesorios::class,
        PinguinoAccesoriosCrossRef::class
    ],
    version = 6,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitosDAO(): HabitosDao
    abstract fun usersDAO(): UserDao
    abstract fun amigosDAO(): AmigosDao
    abstract  fun pinguinoDAO(): PinguinoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitos.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
