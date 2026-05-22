package edu.josakapp.proyectoJosakapp.data.di

import android.content.Context
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.network.AuthService
import edu.josakapp.proyectoJosakapp.data.remote.UserRemoteRepository
import edu.josakapp.proyectoJosakapp.data.repository.PinguinoRepository
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository

object AppModule {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(appContext)
    }

    private val localDatasource: LocalDatasource by lazy {
        LocalDatasource(
            database.usersDAO(),
            database.habitosDAO(),
            database.amigosDAO()
        )
    }

    val authService: AuthService by lazy {
        AuthService()
    }

    val remoteRepository: UserRemoteRepository by lazy {
        UserRemoteRepository()
    }

    val habitoRemoteRepository: edu.josakapp.proyectoJosakapp.data.remote.HabitoRemoteRepository by lazy {
        edu.josakapp.proyectoJosakapp.data.remote.HabitoRemoteRepository()
    }

    val userRepository: UserRepository by lazy {
        UserRepository(
            local = localDatasource,
            authService = authService,
            remote = remoteRepository,
            habitoRemoteRepository = habitoRemoteRepository
        )
    }

    val pinguinoRepository: PinguinoRepository by lazy {
        PinguinoRepository(database.pinguinoDAO())
    }
}
