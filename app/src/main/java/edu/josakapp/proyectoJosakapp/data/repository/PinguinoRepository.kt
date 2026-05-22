package edu.josakapp.proyectoJosakapp.data.repository

import edu.josakapp.proyectoJosakapp.data.local.PinguinoDao
import edu.josakapp.proyectoJosakapp.data.model.Pinguino
import edu.josakapp.proyectoJosakapp.data.model.PinguinoAccesoriosCrossRef
import edu.josakapp.proyectoJosakapp.data.model.PinguinoWithAccesorios

class PinguinoRepository(private val pinguinoDao: PinguinoDao) {

    suspend fun getPinguino(userId: Int): Pinguino? = pinguinoDao.getPinguinoByUserId(userId)

    suspend fun guardarPinguino(pinguino: Pinguino) = pinguinoDao.insertPinguino(pinguino)

    suspend fun registrarCompraRopa(pinguinoId: Int, accesorioId: Int) {
        val crossRef = PinguinoAccesoriosCrossRef(pinguinoId, accesorioId)
        pinguinoDao.insertPinguinoAccesorio(crossRef)
    }

    suspend fun getRopaComprada(userId: Int): PinguinoWithAccesorios? = pinguinoDao.getPinguinoWithAccesorios(userId)
}