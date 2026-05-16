package edu.josakapp.proyectoJosakapp.ui.viewmodel


import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.model.Accesorios
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

    val tiendaDeRopa = listOf(
        Accesorios(id_accesorio = 1,nombre = "Pirata",descripcion = "Sombrero de pirata",imagen = "pirata",precio = 10f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 2, nombre = "Árbol", descripcion = "Sombrero de árbol", imagen = "arbol", precio = 30f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 3, nombre = "Magia", descripcion = "Sombrero mágico", imagen = "magia", precio = 30f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 4, nombre = "Mushroom", descripcion = "Sombrero de hongo", imagen = "mushroom", precio = 30f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 5, nombre = "Fresa", descripcion = "Sombrero de fresa", imagen = "fresa", precio = 30f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 6, nombre = "Calabaza", descripcion = "Sombrero de calabaza", imagen = "calabaza", precio = 40f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 7, nombre = "Flores", descripcion = "Sombrero de flores", imagen = "flores", precio = 50f,tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 8, nombre = "Navidad", descripcion = "Sombrero navideño", imagen = "navidad", precio = 60f,tipo = true, id_pinguino = 0)
    )

    private val _mochilaRopa = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val mochilaRopa = _mochilaRopa.asStateFlow()

    private val _ropaEquipadaRes = MutableStateFlow<Int?>(null)
    val ropaEquipadaRes = _ropaEquipadaRes.asStateFlow()

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