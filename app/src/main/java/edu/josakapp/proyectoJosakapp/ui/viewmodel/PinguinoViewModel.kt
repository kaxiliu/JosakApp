package edu.josakapp.proyectoJosakapp.ui.viewmodel


import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PinguinoViewModel(application: Application) : AndroidViewModel(application) {

   //habitosRepository para acceder a los datos de hábitos y usuarios,
    // reutilizando la lógica ya implementada en HabitosViewModel
    private val habitosRepository: HabitosRepository

    init {
        val database = AppDatabase.getInstance(application)
        //Usamos el mismo LocalDatasource que en HabitosViewModel
        // para mantener la consistencia en el acceso a datos
        val localDatasource = LocalDatasource(database.usersDAO(), database.habitosDAO(), database.amigosDAO())
        val userRepository = AppModule.userRepository
        habitosRepository = HabitosRepository(localDatasource, userRepository)
    }

    private val _nivelSed = MutableStateFlow(1.0f)
    val nivelSed = _nivelSed.asStateFlow()

    //Datos de la mochila del pingüino,
    // donde se almacenan las bebidas compradas por el usuario
    private val _mochila = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val mochila = _mochila.asStateFlow()

    // Función para inicializar el estado del pingüino al cargar la app,
    // calculando la pérdida de sed en función del tiempo transcurrido desde la última sesión
    fun inicializarEstadoPinguino(nivelGuardado: Float, ultimaFecha: Long) {
        val ahora = System.currentTimeMillis()
        val milisegundosPorDia = 24 * 60 * 60 * 1000L
        val diasPasados = (ahora - ultimaFecha).toFloat() / milisegundosPorDia

        val reduccion = diasPasados * 0.1f

        val nuevoNivel = (nivelGuardado - reduccion).coerceIn(0.2f, 1.0f)
        _nivelSed.value = nuevoNivel
    }

    // Función para comprar bebidas y actualizar la mochila del pingüino
    fun comprarBebida(resId: Int, cantidad: Int) {
        val actual = _mochila.value[resId] ?: 0
        _mochila.value = _mochila.value + (resId to (actual + cantidad))
    }

    // Función para usar una bebida de la mochila y reducir el nivel de sed del pingüino
    fun usarBebida(resId: Int, cantidad: Int, userId: Int) {
        val actual = _mochila.value[resId] ?: 0
        if (actual >= cantidad) {
            val nuevaCantidad = actual - cantidad

            if (nuevaCantidad > 0) {
                // Si aún quedan bebidas después de usar, actualizamos la cantidad en la mochila
                _mochila.value = _mochila.value + (resId to nuevaCantidad)
            } else {
                // Si se han usado todas las bebidas, eliminamos la entrada de la mochila
                _mochila.value = _mochila.value - resId
            }
            darDeBeber(cantidad, userId)
        }
    }

    // Función para guardar el estado actual del pingüino en la base de datos,
    // asociándolo al usuario correspondiente
    fun guardarEstadoEnDB(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(getApplication())
            val pinguinoActual = db.usersDAO().getPinguinoByUserId(userId)

            pinguinoActual?.let {
                val pinguinoActualizado = it.copy(
                    nivelSed = _nivelSed.value,
                    ultimaVezSed = System.currentTimeMillis()
                )
                db.usersDAO().insertPinguino(pinguinoActualizado)
            }
        }
    }

    // Función para aumentar el nivel de sed del pingüino al usar una bebida,
    // con un incremento de 0.1 por cada bebida consumida
    fun darDeBeber(cantidad: Int, userId: Int) {
        val nuevoValor = _nivelSed.value + (0.1f * cantidad)
        _nivelSed.value = nuevoValor.coerceAtMost(1.0f)

        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(getApplication())
            // Obtener el pingüino actual del usuario para actualizar su nivel de sed
            // y la última vez que se le dio de beber
            val pinguinoActual = db.usersDAO().getPinguinoByUserId(userId)

            pinguinoActual?.let {
                val pinguinoActualizado = it.copy(
                    nivelSed = nuevoValor, // Guardar el nuevo nivel de sed después de beber
                    ultimaVezSed = System.currentTimeMillis() // Actualizar la última vez que se le dio de beber al pingüino
                )
                //Insertar o actualizar el pingüino en la BD
                db.usersDAO().insertPinguino(pinguinoActualizado)
            }
        }

    }
}