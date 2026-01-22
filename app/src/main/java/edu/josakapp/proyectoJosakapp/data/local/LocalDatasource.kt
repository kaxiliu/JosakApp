package edu.josakapp.proyectoJosakapp.data.local

import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.User

class LocalDatasource(
    private val userDao: UserDao,
    private val habitosDao: HabitosDao
) {

    // USER DAO
    fun getUsersWithHabitos() = userDao.getUserWithHabito()

    fun getAllHabitosFromUserDao() = userDao.getAllUsers()

    suspend fun getUserById(id: Int) = userDao.getUserById(id)

    suspend fun getHabitoByIdFromUserDao(id: Int) = userDao.getHabitoById(id)


    // HABITOS DAO
    fun getAllHabitos() = habitosDao.getAllHabitos()

    fun getUsersWithHabitosFlow() = habitosDao.getUsersWithHabitos()

    fun getHabitoById(id: Int) = habitosDao.getHabitoById(id)

    fun getHabitosByUserId(id: Int) = habitosDao.getUserById(id)

    suspend fun insertUser(user: User) = habitosDao.insertUser(user)

    suspend fun insertHabito(habito: Habito) = habitosDao.insertHabito(habito)

    suspend fun deleteHabito(habito: Habito) = habitosDao.deleteHabito(habito)




}
