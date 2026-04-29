package edu.josakapp.proyectoJosakapp.data.model

fun Habito.toRemote(): HabitoRemote {
    return HabitoRemote(
        id_habito = id_habito,
        nombre = nombre,
        descripcion = descripcion,
        exp_habito = exp_habito,
        frecuencia = frecuencia,
        estado = estado,
        fecha_creacion = fecha_creacion,
        icono = icono,
        colorHex = colorHex
    )
}

fun HabitoRemote.toLocal(userId: Int): Habito {
    return Habito(
        id_habito = id_habito,
        nombre = nombre,
        descripcion = descripcion,
        exp_habito = exp_habito,
        frecuencia = frecuencia,
        estado = estado,
        fecha_creacion = fecha_creacion,
        icono = icono,
        colorHex = colorHex,
        id_usuario = userId
    )
}
