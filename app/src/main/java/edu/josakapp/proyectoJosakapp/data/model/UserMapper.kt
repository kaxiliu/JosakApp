package edu.josakapp.proyectoJosakapp.data.model

fun UserRemote.toLocal(existingId: Int = 0): User {
    return User(
        id_usuario = existingId,    //id_usuario = 0,
        uid = uid, //el UID se mantiene para la sincronización con Firebase
        nombre_usuario = nombre_usuario,
        email = email,
        contrasena = "", // Firebase no devuelve contraseñas
        esPremium = esPremium,
        monedas = monedas,
        fecha_registro = fecha_registro,
        xp_total = xp_total,
        telefono = telefono,
        fotoPerfil = fotoPerfil,
        nivel = nivel,
        puntos = puntos
    )
}


fun User.toRemote(uid: String): UserRemote {
    return UserRemote(
        uid = uid,
        nombre_usuario = nombre_usuario,
        email = email,
        fotoPerfil = fotoPerfil,
        nivel = nivel,
        puntos = puntos,
        monedas = monedas,
        esPremium = esPremium,
        fecha_registro = fecha_registro,
        xp_total = xp_total,
        telefono = telefono
    )
}
