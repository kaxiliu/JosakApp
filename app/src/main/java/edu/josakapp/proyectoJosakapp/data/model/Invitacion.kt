package edu.josakapp.proyectoJosakapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**Clase encargada de la tabla invitación , la cual se utilizara para
 * poder invitar a amigos, contactos, etc*/
@Entity( tableName = "invitacion")
data class Invitacion(
    @PrimaryKey(autoGenerate = true)
    val id_invitacion: Int = 0,
    val id_usuario_emisor: Int,
    val fecha_envio: Date,
    val id_usuario_receptor: Int,
    val estado: Boolean /*Se mostrará si se aceptó o no la invitación*/
)
